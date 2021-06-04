# Download maven-toolbox
curl -L http://bit.ly/maven-toolbox | bash
chmod +x @ci/maven-toolbox

# Configure and build project
./@ci/maven-toolbox configure-properties src/main/resources/config.properties
./@ci/maven-toolbox configure-m2
./@ci/maven-toolbox configure-hibernate
mvn clean install
