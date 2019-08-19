# Cropcert API

API for Cropcert

[![Build Status](https://travis-ci.com/strandls/cropcert-api.svg?branch=master)](https://travis-ci.com/strandls/cropcert-api)

## 🚀 Quick start

```sh
mvn clean                   # Clean application
mvn install                 # Build application WAR and generate OpenAPI SDK
sh @ci/pre-configure-m2.sh  # Creates/Updates Maven `settings.xml` (If using local artificatory please set values in shellfile first)
mvn deploy                  # Deploys WAR to artifactory
```
