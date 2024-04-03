# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](img/phases-2-diagram.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=JYWwhg5gpgBAJmALmAXKSUD0AHAdhAbgCMwBnKANgBYAaYANQCEB5AJQHcAGAaQHEIA9gEERQgHIBlAKoALAKJSIogCIBJUUICeAYSEBNDVAAKVYAA8N2gBqNVAdSsBZUaQAqogDJz2coSGzsBiLK3FIANnIAivSsVLhidmBWejJiUqyuABIAZgAcAFphAFLajmEAXvlCYADWEMAAYqoQuVDljsxUIABuQhD5VppSVAIAjFS5umACigJmAMaOnJoArnYNDZrM2Y565WbcpDXKHpqqYmYgMo4AVvOZVLwNuTcATthEdgCOcKMrAMzlbq4G7KMK4ACcwKsX0Q3SgYAArAAmcqYborUg3f7-ZErKorcpCbTIoTsZE1XgeZhCZScIRQKByejcRxSEBwcoUbrzIpUKTCMxUMBFZRUZS5Tj9VgQZRiRgCGoAaj0jBq2FY50YVAgSvYwFYrGA3U4DW0+UYuWwZky2AhfV4RUc-0yCGUiJ8mGAwE0cHmnGAfj0o3o7FUEmUrFerwg7Ao+QgRWyr3mjNG5Qa2DsRiVyhW8yEuCoiK+RkQvFeDQgpEQFBqnEgVm4MTMmioYiomS+mQgzCMQjgzG0YkiIrgSs4cHIh3+X1BjpW2GD7G0K1GyLAUlGrwOynK2iIIEYUTkUHuLRkvG4nFYmCEDSkNVpzB4YUivDkYVeN0TCT9AmUf5wlGIgalwAQrBqKBNAhTh8lUCFEHobNsBuIgbgAdjMOwzCiCBuH+fJlCITRWysCREG2REIEcKxMkcSI5CMGoig8IRGFJVRumAOAHkQelDmAZEIH+bQwhojDlGQ-4ij0XAvmuV4iiEeY1huOAVk4JDRkiKRXhACQViEVR5kYbIqCsUh3QEBpuCKKwQBAUzVCIbAii+ZF8EYRxNG6cFRk4TJlAEbgVmvDxsigCRuA8XhQ2YZFImUGoQCoVx6F4DDJEyThsCsMRely9QX1Yeg0IEdgQrMVhyKgIR-isdjGC8IV5Fnbo4CZTkVnYRgpF4TReD0bgGgEG45FwEBSDgXBmEcUgwH+MAIVUPQpA8VhHSsBo7Aw3gQCMRxtFbSReAkIxkUQKxEVYL5IgoVRsCeMBcGyDwxDOdSdjsGQagkIgwkQVQzHobojHKBU4GUOREDCfI9GUDFlHmL4PDgIx6CEZhXnof5VWRYAoCkeg5FIUJslUV4rEwcttHoPRcioDDRkYeYIHmKRylwVgzBWbovnGVxNEibRcA8CRkzEeYGg8RBMmYDDgC7DxkdpCgcRqVgil4dganYKx2EyEBGqVKwMM0DgMLAbo5DsMIjGwMJvhk0ZmDGmpckYSJ8lYREjAKbBNBqN6jBAFZcE4MRXgoMwblGeZcjAXIlXmBPOG6XDlBATIBE4FZHCVCRXkyTBtA9yJcj0Kg9EicoUqgMQwgoMAQDCXJcnV3JHAECh6FwFYrEiZElX+DClTi0hEXKDD8nyf4IR6QkjDCTAQiVRxVG4LnMA8GvXm6AQBlGeQVjbA-XjvUZukybg7CkMx-iESYPAoRxumyJBV+4OaPDuoRgAwhADCVBRhmAoOUMINRMjlA8EeIgB5gCvHJBQbI7BNDgmYJoZQDRWB2DEPsbgRA4zaCQawX6vBED5AkKQbIcABB6EcB4dgNoVjAFUFgRECAlSuDsMHTgyIvhCHoBhL4tRsD5BuJEV4iJXDAG4MiLiqgsrdHYA0IQyJMCcFGKMEAnAN6RA0EIaQ9A2DcERNoPQqhVAwAAEQACo7FCAcQAHVwNoGQUBSCkBgI7MAmhgD4BgGBYAEAZCIBgIWRAAhcDAExDARgYQBDzBqDYmAZAIkAChwCICgK8YAYAwidVICEmJ8xoneOyJwCYRAYASFyfCV4GTsBgFeIgYA8xgDNNwOE+xdi6n73aZ4lx3MoD1BrLkmgESViIBkJM3gLcoCpPSf07iqZSBNJaW0jpXSekOOUEgMAKk1mkAcUs7x+zkBHM8eshAyASDkHgEQDJzywjAEiq83AnjEDoNgEIGAABiMprxXqJPYLkgGKwoAoH+CgUgAhXlwAyQAHhIMkiArwBDhzgCgf56K-EAD5nlQG6a8TQpBmkdMCZwAAdLkDJ-yYDgRyTAAQDSIkoBgEiyqrw4DsFeGAbAKAqCcHxa4AQMAFrwhgDM2AATwKpm6ZMv5YcazwGADWfl8xwmdSQDIGAJEYDzBkAUsIxL6iBJkKAaV4qwAwAgAsll2RDUeK8dSmAUhXoCFaeHJAUAwiaEmcoBEMz4ACE8bgAA5OExArxMThL+Uy61zrg1pNwHAGAKrwnkEQIuaVHi0nYEdu0pAwBol5qQHm6CMB9RhDCD4sIfjq3ABmW61xAAKMQUBm0eNeI6ytIBDWYsKTAbIAT022qIAIFp6bACcZIAeD+ACUSLMDct5fy7AhKMlMtgKy3JtT6m5JQEG3VEbvHYFyXC3ABTDWJJWOm8g+9cnPOeeizF2AAVXu6JoGArBRnqpjSWst-z2CWpyRk14k6zAsrZSsiZKzBmkBQL+sZOTe22s+ewGAmJcnUoyX8gAtPi-dj7XgcoANpGGYBIVwABdGAmBsOvFcQAb0Y1ekAUBJnNK8auyZUBwDADCAAXwyZB6De74NrKQ3+jVgHcAZNg72wjxHVmeI5a8GTqG21sYWVxsgpBeMwH42AQTC6FP1IQzAZTFzDmp2uRy6AiApAPu0w+9jUAzM2auV4qzRG4BEA5RIOQXhtCuCw25h1yYBADsYxkrzdmfP4eU5JtTjKVi1vMwMtZvmYDxeORy+YGnfXOdya53J7m9M8a9XATzBzvPeOU-5lA5wgsZHC+V3TPj9OGeM4JmArjziuGYO1xpKWGtEby-Zw1RWclCGmTIMrwKFm1cuQl8bjyOUtbkG1nTHHJlgHm2KqCuAYCDeGwdmZcW6tras8lizUm0mHcVMSzLqmGt3ZIxyi7MgjsvcU7dojQgOXIk4JwFj33fu4BE8SxFz7X25v+V6l60AYAeAEOagFIHm1QHA2J3dvbFM0DGygLl1W+UCuhSKtH1YYABNTUZsw-6AkQBGzANtGmc3Au8ehqAmGIfPdwAu3DBGPsNPI5R6jdHMDkC8aW3ALHduca61VnlImt0CGZfjlTCGUBzcu-90XOvEnmsWxVgF3GDPVYXQQV7lnrPXfy3aqATmXOK5W7Z45OWmtBZC2FxXI6MUxYfVd1bnuktEeJ+6h9Nm21mbGzlybXiOUkODXVebpvlsh499cr3AWts7Yixxx7MzIf9dwGd4vMgs-1YB9rh7-Pju2+y+HlTh6G8vde3ulvQOYAg7B7gZjI2zft5O0JmgGSYfPpfRihH6LGQnep5i8JwHQM44g3McTBOD2vCJ-dtTpOeXk8Ff8Kn6PvFL8razjT2ANPkG6VAdNBqZWV8h8LnLinyPQy8K4OQUuZfFOiVcRH3V01xgz30Qz1yrwNwj3AJQGNyXzbRHzj3AITwd3s06lNVm3TyQOrxu0awCy-zkB-xgBwTmkrzLzsEyG2zkBfwFwAF5gDE93sYCss1Mm9PFa8P9O8lNlMe8+8J800p94d31-kNN00PB-0YB5kONvEV9sdccN8tdCdideBjQODbVXlVUBAnUTU7UFlSA39RdD0YAyMPxaN6N7UOMgCntG9t1N8681NIDuD39YDvtiUtlfVECbDiVkDWD1smCOVeBncStXg21IDId3ca98DAtgs5BQsh8FlXFSDHByDKDqDrVjsGDvD5MmDODYCo9StfC3s8jPsCjQizM7ClDYDpDPFnDDcpNHMajSAM8OMii7cJs0Ck8pDncmiWiPNcCw8-MAsfc4iws7ESDWAyDLCODXE0jWAaD3M6DFcBic8W9idpjvFTCFlY9JlqU9iaN2DmDW9SMNiTCaidiYA9jqUDiDdeCUA+8WM9CZCYAOVXEYATDB83j3inioBVBlBJkvj3iscckQizdAT9UG1klQTOtwTpixAHUvihM6NXFodBC4cZ8RDsAVh3hTUYAU9fUpCHU5CwN18oMqiSNJlicD810KcT98V8ScludGVecfjDCWCxcTCJdzDMBpjrCS8BcWM4SFkRNKiwC-Ddd5s6j2Sdc3Duli0ckvD+Tjs2jm8OjQ8psgiXdSscDcjoiRj4j-dkjyDZiqD5iMjiUsilSXtdT6jUsQjY9DiSiOT7SKiNcd0xS3tAj9CpSHDENptU9zjGNJkhTWjHT7d1SujCsAztigyfj4TQzdShiUB9Swt3MJipj9CKDTSFiFk6CQycdEyNt88wsK9Tj218zJk3cVjEtbS-TziVSOC1jt8HMFk-i6i7je9QdHjpjVB01UTYdp830AVNA-VQUYAigBA6caiAUAYwBkkFCySPS4NYDqSj9Kd8V6Bck3lCZvEZkK1n9yUzxtyH8fiGd-1SB9s01Jk3k0lB1Ele11UJVz0OlR0H99s4ApxL95gTU910ln8NMvhIVxl00yl7yk1n9pi3VVAnVwI7yvVadvFDyXzCZ01n9GMEKYAbhJzPlx1ucTsBAiAH0Gk3VXBLVvEAKgLwlHyeJ+NsA3Tuk2TjjxcpBuTeSr1siWNl4-FcltB4UvVgzWzlARM8cPSdcpl9dt8XDxTK93D5SoBFSfsBcGz-DOi6zgiXMdTVLc9kzYiDTC9YAjTvssz0iolMjGCtKmzpKXSwyjDSMyiHSRKJN8imifTI8axNlziez-ifjeL7zKyH1lLUCIy-TGjtivKF0YBqz1smtaldKxj0yUjTi5iaCeI8zBKoqnSxL6yQD3SnLpLVAGhTzGNucNMsNcBs16VfTw8P8P4whyAwy1Ts8ujFxbkoBPLBL-KJk4LXhIi8CkypAjBlAhBiCyzcAgs-cH0liH14yldQKvUprckZrjKzTUreAhBHA5A2ybTpSHsY1IUbL2TD09qcdbjAdgdQcBCByMlhCAU8Vv0xIEQ0NC1Xlvy2kgNgSccFz7DlCVzV01y6SHqWldz81bkwB7koA3Vf0QBd1uda0RsLyfiEaXpx1siDC8MpKOSyNCCf8pd-Mcrvr8iBwQAAl-1+U3r5NoDfSUAMDnc6pa0HT49wymq-SabZt6berBiNsYBXBWApAxBdBiC7EMrLLPTpVY0TqUCRbjDjr2yzrOz+9mMR9+znlJ1EAol-BNl2lOkXpEB1kgA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
