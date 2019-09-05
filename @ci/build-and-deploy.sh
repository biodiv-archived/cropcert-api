#!/bin/bash

# Minimal script that updates default credintials with prod

echo "⚙️ Updating configuration"
sed 's|>postgres<|>'$DB_USER'<|g' -i WebContent/WEB-INF/classes/hibernate.cfg.xml
sed 's|>postgres123<|>'$DB_PASSWORD'<|g' -i WebContent/WEB-INF/classes/hibernate.cfg.xml
sed 's|localhost|'$DB_HOST'|g' -i WebContent/WEB-INF/classes/hibernate.cfg.xml
sed 's|5432|'$DB_PORT'|g' -i WebContent/WEB-INF/classes/hibernate.cfg.xml
sed 's|cropcert|'$DB_NAME'|g' -i WebContent/WEB-INF/classes/hibernate.cfg.xml

echo "📦 Building package"
/opt/apache-maven/bin/mvn clean package

echo "🚀 Uploading to tomcat"
curl --upload-file target/cropcert.war http://$SERVER_1/manager/text/deploy?path=/cropcert&update=true
