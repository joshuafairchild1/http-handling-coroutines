#!/usr/bin/env bash

ab -n 1000 -c 10 http://localhost:$1/ >> load_test_async_output.txt &
ab -n 1000 -c 10 http://localhost:$2/ >> load_test_sync_output.txt
