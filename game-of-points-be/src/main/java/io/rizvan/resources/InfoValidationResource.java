package io.rizvan.resources;

import io.rizvan.beans.Weapon;
import io.rizvan.beans.WeaponCache;
import io.rizvan.beans.actors.player.PlayerAnswers;
import jakarta.inject.Inject;
import jakarta.json.*;
import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


@Path("/info-validation")
public class InfoValidationResource {

    @Inject
    Jsonb jsonb;
    @Inject
    WeaponCache weaponCache;
    @Inject
    PlayerAnswers playerAnswers;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleDialogflowRequest(String requestBody) {
        System.out.println(requestBody);
        // Parse the incoming JSON request
        JsonObject request = parseJsonRequest(requestBody);

        var formParams = extractFormParameters(request);
        var validatedParams = validateFormParameters(formParams);
        addFromParametersToPlayerAnswers(validatedParams);
        // Safely extract query text
        String queryText = extractQueryText(request);

        // Create a successful response
        JsonObject jsonResponse = createSimpleResponse("Your request was: " + queryText, validatedParams);
        System.out.println(jsonResponse);
        System.out.println(playerAnswers);
        return Response.ok(jsonResponse).build();
    }

    private JsonObject parseJsonRequest(String requestBody) {
        try {
            return jsonb.fromJson(requestBody, JsonObject.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON format", e);
        }
    }

    private String extractQueryText(JsonObject request) {
        return request.getString("text", "No text provided.");
    }

    private List<FormParameter> extractFormParameters(JsonObject request) {
        return request.getJsonObject("pageInfo")
                .getJsonObject("formInfo")
                .getJsonArray("parameterInfo")
                .stream()
                .map(parameter -> {
                    var paramJson = parameter.asJsonObject();
                    var displayName = paramJson.getString("displayName");
                    Object value = paramJson.get("value");

                    String valueAsString;
                    if (value instanceof Number) {
                        valueAsString = value.toString();
                    } else {
                        valueAsString = String.valueOf(value); // Handle unexpected types by converting to string
                    }
                    valueAsString = valueAsString.replace("\"", "");

                    return new FormParameter(
                            displayName,
                            paramJson.getBoolean("required"),
                            paramJson.getString("state"),
                            valueAsString,
                            paramJson.getBoolean("justCollected")
                    );
                })
                .toList();
    }

    private List<FormParameter> validateFormParameters(List<FormParameter> formParameters) {
        var validatedFormParameters = new ArrayList<FormParameter>();

        formParameters.forEach(param -> {
            var formParameter = new FormParameter(param);
            var isValid = validate(formParameter);
            formParameter.setState(isValid ? ParamState.VALID : ParamState.INVALID);
            validatedFormParameters.add(formParameter);
        });

        return validatedFormParameters;
    }

    private void addFromParametersToPlayerAnswers(List<FormParameter> formParameters) {
        formParameters.forEach(param -> {
            if (param.getState() == ParamState.VALID) {
                addParamToPlayerAnswers(param);
            }
        });
    }

    private void addParamToPlayerAnswers(FormParameter formParameter) {
        switch (formParameter.getDisplayName()) {
            case USER_MOOD ->
                    playerAnswers.setMood(formParameter.getValue());
            case WEAPON_NAME ->
                    playerAnswers.setWeaponName(formParameter.getValue());
            case WEAPON_SPEED ->
                    playerAnswers.setSpeed(Double.parseDouble(formParameter.getValue()));
            case WEAPON_DAMAGE -> {
                var valueDouble = Double.parseDouble(formParameter.getValue());
                var value = Math.toIntExact((long) valueDouble);
                playerAnswers.setDamage(value);
            }
            case WEAPON_USES -> {
                var valueDouble = Double.parseDouble(formParameter.getValue());
                var value = Math.toIntExact((long) valueDouble);
                playerAnswers.setUses(value);
            }
            case WEAPON_RANGE ->
                    playerAnswers.setRange(Double.parseDouble(formParameter.getValue()));
            case WEAPON_RECHARGE_TIME -> {
                var valueDouble = Double.parseDouble(formParameter.getValue());
                var value = (long) valueDouble;
                playerAnswers.setRechargeTime(value);
            }
        }
    }

    private JsonObject createSimpleResponse(String fulfillmentText, List<FormParameter> formParameters) {
        var invalidParams = formParameters.stream().filter(param -> param.state.equals(ParamState.INVALID)).toList();
        JsonArrayBuilder parameterInfoArray = Json.createArrayBuilder();
        for (var param: formParameters) {
            parameterInfoArray.add(Json.createObjectBuilder()
                    .add("displayName", param.getDisplayName().getName())
                    .add("required", Boolean.toString(param.isRequired()))
                    .add("state", param.getState().toString())
                    .add("value", param.getValue())

            );
        }

        // Build the "form_info" object
        JsonObjectBuilder formInfo = Json.createObjectBuilder()
                .add("parameter_info", parameterInfoArray);

        // Build the "page_info" object
        JsonObjectBuilder pageInfo = Json.createObjectBuilder()
                .add("form_info", formInfo);

        // Build the "session_info" object
        var parameters = Json.createObjectBuilder();
        for (var param: invalidParams) {
            parameters.add(param.displayName.getName(), "null");
        }
        JsonObjectBuilder sessionInfo = Json.createObjectBuilder().add("parameters", parameters);

        // Build the final response object
        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("page_info", pageInfo)
                .add("session_info", sessionInfo);


        return response.build();
    }

    /**
     * {
     * "page_info": { "form_info": { "parameter_info": [ { "display_name": "order_number", "required": "true", "state": "INVALID", "value": "123" } ] } },
     * "session_info": { "parameters": { "order_number": "null" } }
     * }
     */


    public boolean validate(FormParameter parameter) {
        var weapons = weaponCache.getWeapons();
        switch (parameter.displayName) {
            case USER_MOOD -> {
                return validateUserMood(parameter.getValue());
            }
            case WEAPON_NAME -> {
                return validateWeaponName(parameter.getValue(), weapons);
            }
            case WEAPON_SPEED -> {
                return validateWeaponSpeed(parameter.getValue(), weapons);
            }
            case WEAPON_DAMAGE -> {
                return validateWeaponDamage(parameter.getValue(), weapons);
            }
            case WEAPON_RECHARGE_TIME -> {
                return validateWeaponRechargeTime(parameter.getValue(), weapons);
            }
            case WEAPON_USES -> {
                return validateWeaponUses(parameter.getValue(), weapons);
            }
            case WEAPON_RANGE -> {
                return validateWeaponRange(parameter.getValue(), weapons);
            }
            default ->
                    throw new IllegalArgumentException("Invalid FormParameter: " + parameter.getDisplayName().getName());
        }
    }

    public boolean validateWeaponName(String weaponName, List<Weapon> weapons) {
        return weapons.stream()
                .anyMatch(w -> w.getName().equalsIgnoreCase(weaponName));
    }

    public boolean validateWeaponSpeed(String speed, List<Weapon> weapons) {
        DecimalFormat df = new DecimalFormat("#.##");
        Double speedDouble = Double.valueOf(df.format(Double.parseDouble(speed)));

        return weapons.stream().anyMatch(w -> Double.valueOf(df.format(w.getSpeedModifier())).equals(speedDouble));
    }

    public boolean validateWeaponDamage(String damage, List<Weapon> weapons) {
        try {
            var damageDouble = Double.parseDouble(damage);
            return weapons.stream().anyMatch(w -> (double) w.getDamage() == damageDouble);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean validateWeaponRechargeTime(String rechargeTime, List<Weapon> weapons) {
        try {
            var rechargeTimeDouble = Double.parseDouble(rechargeTime);
            return weapons.stream().anyMatch(w -> (double) w.getRechargeTimeMilli() == rechargeTimeDouble);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean validateWeaponUses(String uses, List<Weapon> weapons) {
        try {
            var usesDouble = Double.parseDouble(uses);
            return weapons.stream().anyMatch(w -> (double) w.getAmmoCapacity() == usesDouble);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean validateUserMood(String feelings) {
        String regex = "^(\\b\\w{3,}\\b(?:,\\s*)?)+$";
        return feelings!= null && feelings.matches(regex);
    }

    public boolean validateWeaponRange(String range, List<Weapon> weapons) {
        DecimalFormat df = new DecimalFormat("#.##");
        Double rangeDouble = Double.valueOf(df.format(Double.parseDouble(range)));
        return weapons.stream().anyMatch(w -> Double.valueOf(df.format(w.getRange())).equals(rangeDouble));
    }

    public enum ValidationSubject {
        WEAPON_NAME,
        WEAPON_SPEED,
        WEAPON_DAMAGE,
        WEAPON_USES,
        WEAPON_RANGE,
        WEAPON_RECHARGE_TIME,
        USER_MOOD
    }

    public enum ParameterName {
        WEAPON_NAME("weapon-name"),
        WEAPON_SPEED("weapon-speed"),
        WEAPON_DAMAGE("weapon-damage"),
        WEAPON_USES("weapon-uses"),
        WEAPON_RANGE("weapon-range"),
        WEAPON_RECHARGE_TIME("weapon-recharge-time"),
        USER_MOOD("user-mood");

        private final String name;

        ParameterName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static ParameterName fromName(String name) {
            for (ParameterName parameterName : ParameterName.values()) {
                if (parameterName.getName().equals(name)) {
                    return parameterName;
                }
            }
            throw new IllegalArgumentException("Invalid parameter name: " + name);
        }
    }

    public enum ParamState {
        FILLED,
        VALID,
        INVALID,
        UPDATED
    }


    public class FormParameter {
        private final ParameterName displayName;
        private final boolean required;
        private ParamState state;
        private final String value;
        private final boolean justCollected;

        public FormParameter(String displayName, boolean required, String state, String value, boolean justCollected) {
            this.displayName = ParameterName.fromName(displayName);
            this.required = required;
            this.state = ParamState.valueOf(state);
            this.value = value;
            this.justCollected = justCollected;
        }

        public FormParameter(FormParameter other) {
            this.displayName = other.displayName;
            this.required = other.required;
            this.state = other.state;
            this.value = other.value;
            this.justCollected = other.justCollected;
        }

        public ParameterName getDisplayName() {
            return displayName;
        }

        public boolean isRequired() {
            return required;
        }

        public ParamState getState() {
            return state;
        }

        public void setState(ParamState state) {
            this.state = state;
        }

        public String getValue() {
            return value;
        }

        public boolean isJustCollected() {
            return justCollected;
        }
    }



}
