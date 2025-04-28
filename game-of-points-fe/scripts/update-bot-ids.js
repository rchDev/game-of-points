#!/usr/bin/env node
/**
 * update‑bot‑ids.js
 *
 * Rewrites the first <df‑messenger> tag it finds in ./index.html,
 * replacing project‑id and agent‑id attributes with the values
 * given on the command line.
 *
 * Usage: node update‑bot‑ids.js --project-id=NEW_PROJ --agent-id=NEW_AGENT
 */

import fs from 'fs/promises';
import path from 'path';
import { fileURLToPath } from 'url';
import { argv } from 'process';

// ---------- simple CLI parsing --------------------------------------------
const args = argv.slice(2).reduce((map, arg) => {
    const [k, v] = arg.replace(/^--/, '').split('=');
    map[k] = v;
    return map;
}, {});

const { 'project-id': projectId, 'agent-id': agentId } = args;

if (!projectId) {
    console.error('❌  missing --project-id parameter');
}

if (!agentId) {
    console.error('❌  missing --agent-id parameter');
}

if (!agentId || !projectId) {
    console.error('💡 Usage: npm run update-bot-ids -- --project-id=<id> --agent-id=<id> or, ' +
        '\n   in case of npm install: PROJECT_ID=<ID> AGENT_ID=<id> npm install');
}

// ---------- locate & read index.html --------------------------------------
const __filename = fileURLToPath(import.meta.url);
const __dirname  = path.dirname(__filename);

const htmlPath   = path.join(__dirname, '..', 'index.html');
const html       = await fs.readFile(htmlPath, 'utf8');

// ---------- do the replacement (one pass, regex is safe) ------------------
const updated = html
    .replace(/(<df-messenger[^>]*\bproject-id=")[^"]*(")/i,
        `$1${projectId}$2`)
    .replace(/(<df-messenger[^>]*\bagent-id=")[^"]*(")/i,
        `$1${agentId}$2`);

await fs.writeFile(htmlPath, updated, 'utf8');
console.log(`✔ Updated project‑id and agent‑id in ${path.relative(process.cwd(), htmlPath)}`);
