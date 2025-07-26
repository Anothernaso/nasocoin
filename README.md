# NasoCoin 💰 #

*A digital currency*

## How to build 🏗️🤔 ##
#### Step 1: Install SDKMAN!, OpenJDK 24.0.2 and Gradle 8.14.3 ####
``` curl -s "https://get.sdkman.io" | bash ```
<small>Install SDKMAN!</small>

``` sdk install java 24.0.2-open ```
<small>Install OpenJDK 24.0.2</small>

``` sdk install gradle 8.14.3 ```
<small>Install Gradle 8.14.3</small>

#### Step 2: Prepare the project ####
``` git clone https://github.com/Anothernaso/nasocoin ```
<small>Clone the remote Git repository</small>

``` cd nasocoin ```
<small>Enter the project directory</small>

#### Step 3: Build the Java archives using Gradle ####
``` gradle buildAll ```

You can now find the files at:
``` server/build/libs/ ```
and
``` client/build/libs/ ```
