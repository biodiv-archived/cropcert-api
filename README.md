# Cropcert API

API for Cropcert

![Build + SDK](https://github.com/strandls/cropcert-api/workflows/Build%20+%20SDK/badge.svg?branch=master)

## 🚀 Quick start

```sh
mvn clean                   # Clean application
mvn install                 # Build application WAR and generate OpenAPI SDK
sh @ci/pre-configure-m2.sh  # Creates/Updates Maven `settings.xml` (If using local artificatory please set values in shellfile first)
mvn deploy                  # Deploys WAR to artifactory
```
