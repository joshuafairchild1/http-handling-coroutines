#!/usr/bin/env bash

ab -n 10000 -c 10 http://localhost:$1/