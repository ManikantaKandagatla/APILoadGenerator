# APILoadGenerator

This webapp serves as an utility to fire web requests for the provided API details. 

Sample API load generator. Supports parallel request execution with the number of threads specified in the request

Currently the Utility just follows FIRE AND FORGET.

Refer :
https://github.com/ManikantaKandagatla/APILoadGenerator/blob/master/src/main/resources/sample/request.json for firing sample web api request. 
https://github.com/ManikantaKandagatla/APILoadGenerator/blob/master/src/main/resources/sample/sample_curl.txt for sample curl

You can update the request as per the API details by adding details to headers, body, wherever needed.
This utility parallely calls threads * (no.of requests) specified in the load request. 

Todo: 
1. Persisit request in queue and handle it in async instead of handling it async via single thread executor and provide an execution_id to see the results. 
2. Currently user can only validate whether APIs in request are hit or not only checking his application logs
3. Provide status of each api call fired from the utility
4. Instead of threads* (no.of requests) parallel api calls, introduce request group and have parallel/serial execution of api calls
5. Max load / thread capacity limit for the utility.
