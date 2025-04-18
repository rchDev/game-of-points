from py4j.java_gateway import JavaGateway, GatewayParameters, CallbackServerParameters
from pgmpy.models import DiscreteBayesianNetwork
from pgmpy.factors.discrete import TabularCPD
from pgmpy.inference import VariableElimination
import logging
from py4j.java_collections import MapConverter


# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class BayesianNetworkManager:
    def __init__(self):
        self.model = DiscreteBayesianNetwork()

    def __str__(self):
        return "BayesianNetworkManager instance"

    def __repr__(self):
        return self.__str__()

    def toString(self):
        return self.__str__()

    def set_gateway(self, gateway):
        self.gateway = gateway

    def add_nodes(self, nodes):
        """
        Funkcija prideda nauja atsitiktini kintamaji i Bajeso tinkla
        :param nodes:
        :return:
        """
        try:
            nodes = list(nodes.toArray())
            logger.info(f"Adding nodes: {nodes}")
            self.model.add_nodes_from(nodes)
            logger.info("Nodes added successfully.")
        except Exception as e:
            logger.error(f"Error adding nodes: {e}")
            raise

    def add_edges(self, edges):
        """
        Funkcija sukuria sarysius tarp atsitiktiniu kintamuju
        :param edges:
        :return:
        """
        try:
            edges = [tuple(edge) for edge in list(edges.toArray())]
            logger.info(f"Adding edges: {edges}")
            self.model.add_edges_from(edges)

            logger.info("Edges added successfully.")
        except Exception as e:
            logger.error(f"Error adding edges: {e}")
            raise

    def add_cpd(self, variable, variable_card, values, evidence=None, evidence_card=None):
        """
        Funkcija prideda salygini tikimybiu skirstini, pasirinktam atsitiktiniam kintamajam
        """
        values = [list(value) for value in list(values)]

        if evidence is not None:
            evidence = list(evidence)

        if evidence_card is not None:
            evidence_card = list(evidence_card)

        logger.info(f"Adding variable: {variable}")
        logger.info(f"Adding variable_card: {variable_card}")
        logger.info(f"Adding values: {values}")
        logger.info(f"type of values: {type(values)}")
        logger.info(f"evidence: {evidence}")
        logger.info(f"type of evidence: {type(evidence)}")

        try:
            cpd = TabularCPD(variable=variable, variable_card=variable_card, values=values,
                             evidence=evidence, evidence_card=evidence_card)
            self.model.add_cpds(cpd)
            logger.info(f"CPD added for {variable}.")
        except Exception as e:
            logger.error(f"Error adding CPD: {e}")
            raise

    def finalize_model(self):
        """Funkcija, kuri patikrina modelio teisinguma ir grazina ismeta exception, jeigu modelis nera teisingas"""
        try:
            assert self.model.check_model()
            # Variable elimination o, ne sampling metodai, nes modelis yra pakankamai mazas
            self.infer = VariableElimination(self.model)
            logger.info("Model finalized successfully.")
        except Exception as e:
            logger.error(f"Error finalizing model: {e}")
            raise

    def map_query(self, query, evidence):
        """
        Funkcija grazina map_query rezultata, kuris yra tiketiniausia jungtini tikimybiu skirstini skirstini.,
        :param query: atsitiktiniai kintamieji, kuriu nezinome ir norime suzinoti
        :param evidence: atsitiktiniai kintamieji, kuriu reiksmes zinome
        :return: grazina key, value poru struktura, kuria py4j biblioteka Java puseje konvertuos i Java Map.
        """
        try:
            logger.info(f"query type: {type(query)}")
            queryList = list(query)
            logger.info(f"evidence: {type(evidence)}")
            evidence_dict = {entry[0]: int(entry[1]) for entry in list(evidence)}
            logger.info(f"evidence: {type(evidence_dict)}")
            for key, value in evidence_dict.items():
                logger.info(f"dict key: {key}")
                logger.info(f"dict key type: {type(key)}")
                logger.info(f"dict value: {value}")
                logger.info(f"dict value type: {type(value)}")
            result = self.infer.map_query(variables=queryList, evidence=evidence_dict)
            logger.info(f"MAP query result: {result}")
            logger.info(f"POST MAP query")

            java_map = MapConverter().convert(result, self.gateway._gateway_client)
            return java_map
        except Exception as e:
            logger.error(f"Error performing MAP query: {e}")
            raise

if __name__ == "__main__":
    # Inicializuojame Bajeso tinkla
    manager = BayesianNetworkManager()

    # Startuojame py4j serveri
    gateway = JavaGateway(
        gateway_parameters=GatewayParameters(port=25333, auto_convert=True),
        callback_server_parameters=CallbackServerParameters(port=25334),
        python_server_entry_point=manager
    )

    # Paduodame py4j serveri bajeso tinklo klasei, vidiniam vartojimui (Python i Java reiksmiu konvertavimui)
    manager.set_gateway(gateway)

    logger.info("Python server is ready.")
