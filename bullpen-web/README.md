# Running the WebApp
First, ensure you have everything built by going to the top level applicatant-tracking-service directory and running:

    cd applicant-tracking-system
    mvn clean install

Then:

    cd ats-web
    mvn clean package tomcat7:run-war


# Pushing to Production
We are using Cloudfoundry to run our production environment.  Right now, we really only have a test environment that is publicly addressable, but we will call that our production environment.  This is how you update it. 

First, you need to log into the Cloudfoundry instance:

    cf login -a api.run.pivotal.io
    API endpoint: api.run.pivotal.io
    Email> andrew.serff@acesinc.net
    Password>
    Authenticating...
    OK
    Targeted org aces
    Targeted space development
    API endpoint:   https://api.run.pivotal.io (API version: 2.14.0)
    User:           andrew.serff@acesinc.net
    Org:            aces
    Space:          development

Now make sure everything is built

    cd applicant-tracking-system
    mvn clean install

Now you can push to Cloudfoundry.  Note that this will destroy the currently running instance and start the new one.  It will NOT destroy any of the data that is sorted in our MongoDB instance.  To update, do this:

    cf push

Once that completes, the Application should now be addressible from here:
[https://aces-ats.cfapps.io](https://aces-ats.cfapps.io)

