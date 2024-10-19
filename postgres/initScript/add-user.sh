#!/bin/bash

set -e
psql -v ON_ERROR_STOP=1 -v usr=${PGUSER} -v PGPASSWORD=\'${PGPASSWORD}\' -v POSTGRES_DB=${POSTGRES_DB} --username ${POSTGRES_USER} --dbname ${POSTGRES_DB} <<-EOSQL
    CREATE SCHEMA new;
    SET search_path TO new;
EOSQL