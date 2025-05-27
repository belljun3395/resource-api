#!/bin/sh


sleep 5

export VAULT_ADDR='http://resources-vault:8200'
export VAULT_TOKEN='root'

echo "Initializing Vault..."

echo "Put local.json in Vault..."
VAULT_TOKEN=root vault kv put secret/resources-service/local @/vault/data/local.json

echo "Finishing Vault initialization..."