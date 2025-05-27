PASSWORD=$(expr $1)
PORT=$(expr $2)

CONF_FILE="/tmp/redis.conf"

echo "port $PORT
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
appendonly yes
loglevel debug
requirepass $PASSWORD
masterauth  $PASSWORD
protected-mode no
" >> $CONF_FILE

redis-server $CONF_FILE