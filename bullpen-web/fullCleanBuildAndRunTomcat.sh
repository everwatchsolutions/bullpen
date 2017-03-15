#cd ..; mvn clean install; cd ats-web; mvn tomcat7:run-war
cd ~/Desktop/apache-tomcat-8.0.35/webapps/ #CD into your tom cat webapps folder
rm -f ROOT.war #remove the war file and folder
rm -rf ROOT
cd ~/Desktop/bullpen/ # move into ats
mvn clean install
cd bullpen-web/target/
mv bullpen-web.war ~/Desktop/apache-tomcat-8.0.35/webapps/ROOT.war #move and rename the war file to root in the webapps folder
cd ~/Desktop/apache-tomcat-8.0.35/bin/ #move into the bin folder
sh catalina.sh run
