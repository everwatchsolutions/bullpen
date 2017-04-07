cd  /opt/apache-tomcat-8.5.13/webapps/ #CD into your tom cat webapps folder
rm -f ROOT.war #remove the war file and folder
rm -rf ROOT
cd ..
cd ..
cd bullpen/ # move into ats
mvn clean install
cd bullpen-web/target/
mv bullpen-web.war /opt/apache-tomcat-8.5.13/webapps/ROOT.war #move and rename the war file to root in the webapps folder
cd /opt/apache-tomcat-8.5.13/bin/ #move into the bin folder
sh catalina.sh run
