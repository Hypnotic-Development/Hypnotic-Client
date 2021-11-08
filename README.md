![](https://img.shields.io/badge/0%25-optimized-brightgreen?style=for-the-badge&logo=appveyor)
![](https://img.shields.io/badge/Made%20by-skript%20kiddies-red?style=for-the-badge&logo=appveyor)
![](https://img.shields.io/badge/100%25-spaghetti-orange?style=for-the-badge&logo=appveyor)
![](https://img.shields.io/badge/works-sometimes-blue?style=for-the-badge&logo=appveyor)
![](https://img.shields.io/badge/50%25-Original-blue?style=for-the-badge&logo=appveyor)

# Hypnotic Client  
## gaming awards frame winner 2004/2006/2007  
https://hypnotic.dev is our website that isn't so bad anymore
  
## About
"Utility Mod" for Minecraft 1.17.1  
Use at your own risk, we are not responsible for any punishments on any server  
  
## Requirements  
Minecraft 1.17.1  
Java Runtime version 16 or higher  
Fabric Loader 0.12.4 or higher  
  
## Authors  
BadGamesInc - actual knowledge  
KawaiiZenbo - idk
  
### Other Credits  
[Fabric](https://fabricmc.net/)  
[Baritone](https://github.com/MeteorDevelopment/baritone)  
Whoever else we stole code from  

## Support
Join our "amazing" discord for support https://discord.gg/aZStDUnb29

## How to compile  
Download and extract the Hypnotic client source code  
Open Command Prompt/Terminal  
Navigate to where you extracted the Hypnotic Source  
Run `gradlew build`  
After Gradle is done, the complete mod JAR should be located in -  
`<hypnotic repo folder>/build/libs/Hypnotic-r1000.jar` **Any other jar in that folder will not work**  
  
## How to import into Eclipse  
Install Eclipse from https://www.eclipse.org/ide/  
Download and extract the Hypnotic client source code  
Open Command Prompt/Terminal  
Navigate to where you extracted the Hypnotic Source  
Run `gradlew eclipse`, then, optionally, run `gradlew gensources`  
Import the folder into Eclipse as `Existing Projects into workspace`  
Click on the arrow next to the Debug button in the toolstrip, then click `Debug configurations`  
Expand `Java Application`, then double-click `Hypnotic-Fabric-1.17_client`  
Hypnotic should now run along with all of your changes  

## Compiled Builds  
Please get compiled build from the latest Github Actions artifact, we do not keep up regular releases.  
**Ensure you select specifically `Hypnotic-r1000.jar` from `Artifacts.zip`, any others will not work**  
  
