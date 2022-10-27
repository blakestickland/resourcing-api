# resourcing-api

REST API created in Java using Spring Boot that allows clients to manage their Temp staff job assignments. 

## Links
### Github Repository
https://github.com/blakestickland/resourcing-api

## MVP:

* An API that allows clients to retrieve:
  - all jobs in the system.
  - a job by providing an id.
  - all jobs that have either been assigned to Temps or all jobs yet-to-be assigned.
* An API that allows clients to create a new job (and assign a Temp).
* An API that allows clients to update a job (and assign a Temp). 
* An API that allows clients to delete a job.  
* A SQL database to persist data.
* Only one Temp can be assigned to a Job. 
* A Temp can have many jobs, as long as the dates don't clash.

## Endpoints
***
  * "/"
    * GET   Health Check    returns “Hello World!!”         200 OK
***
### Jobs
  * "/jobs"
    * GET   Return all jobs. Returns JSON array of results. 200 OK or 204 NO CONTENT
***
  * "/jobs?assigned={TRUE | FALSE}"
    * GET   Return all jobs that have been assigned.        200 OK or 204 NO CONTENT
            Returns JSON array
***
  * "/jobs/{job_id}"
    * GET   Returns job associated with job id.             200 OK or 404 NOT FOUND
***
***
  * "/jobs"
    * POST  Create job; (requires a body)                   201 created or 400 BAD REQUEST
***
  * "/jobs/{job_id}"
    * PATCH Update job; Alter name, start date, end date    200 OK or 400 BAD REQUEST
                        or Temp assigned to job
                        requires a body
***
***
  * "/jobs/{job_id}"
    * DELETE  Delete job by id                              204 NO CONTENT or 404 NOT FOUND 
***
### Temps
  * "/temps"
    * GET   Return all temps.                               200 OK or 204 NO CONTENT
            Returns JSON array of results.
***
  * "/temps?jobId={job_id}"
    * GET   Return all temps available based on specified   200 OK or 204 NO CONTENT or 404 NOT FOUND
            job's date range.
            Returns JSON array of results.
***
  * "/temps/{temp_id}"
    * GET   Returns temp associated with temp id.           200 OK or 404 NOT FOUND
***
***
  * "/temps"
    * POST  Create temp; (requires a body)                  201 created or 400 BAD REQUEST
***
***
  * "/temps/{temp_id}"
    * DELETE  Delete temp by id                             204 NO CONTENT or 404 NOT FOUND 
***

### Example JSON Body for POST request to create new Job database entry: 

```
    {
        "name": "Develop a REST API using Java / Spring Boot with SQL persistence",
        "startDate": "2022-09-01",
        "endDate": "2022-10-01"
    }
```
        or 
```
    {
        "name": "Develop a REST API using Java / Spring Boot with SQL persistence",
        "startDate": "2022-09-01",
        "endDate": "2022-10-01",
        "tempId": 5
    }
```
### Example JSON Body for POST request to create new Temp database entry: 

```
    {
        "firstName": "Alexa",
        "lastName": "Google"
    }
```

NOTE: submitting just the required fields ("name", "start date", and "end date") can be done multiple times as there may be multiple Temps needed for a Job. At the moment, only one Temp can be assigned to a Job at a time.
            
NOTE: you cannot submit a new Job nor alter a Job if start date is after the end date or dates clash with the job dates associated with the assigned Temp.

## Build and Deploy

* Build a version of the code by running: 
  
```
    ./mvnw clean install
```

or 

```
    ./mvnw clean install -DskipTests
```
* Deploy the the built project by uploading the jar file in the target directory (e.g. target/postcodesau-0.0.1-SNAPSHOT.jar).

## Future development
* Test deployment to AWS cloud environment. Add notes to README after successful deployment.
* Add a few example database entries of seeding data to serve as an example for client.
* Add tests to automate testing of code.
* Add protected paths (admin?) to allow client to CREATE or UPDATE database entries only if they have login details.


## Contributor
* Blake Stickland https://github.com/blakestickland