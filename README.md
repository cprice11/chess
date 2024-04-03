# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](img/phase-2-diagram.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=JYWwhg5gpgBAJmALmAXKSUD0AHAdhAbgCMwBnKANgBYAaYANQCEB5AJQHcAGAaQHEIA9gEERQgHIBlAKoALAKJSIogCIBJUUICeAYSEBNDVAAKVYAA8N2gBqNVAdSsBZUaQAqogDJz2coSGzsBiLK3FIANnIAivSsVLhidmBWejJiUqyuABIAZgAcAFphAFLajmEAXvlCYADWEMAAYqoQuVDljsxUIABuQhD5VppSVAIAjFS5umACigJmAMaOnJoArnYNDZrM2Y565WbcpDXKHpqqYmYgMo4AVvOZVLwNuTcATthEdgCOcKMrAMzlbq4G7KMK4ACcwKsX0Q3SgYAArAAmcqYborUg3f7-ZErKorcpCbTIoTsZE1XgeZhCZScIRQKByejcRxSEBwcoUbrzIpUKTCMxUMBFZRUZS5Tj9VgQZRiRgCGoAaj0jBq2FY50YVAgSvYwFYrGA3U4DW0+UYuWwZky2AhfV4RUc-0yCGUiJ8mGAwE0cHmnGAfj0o3o7FUEmUrFerwg7Ao+QgRWyr3mjNG5Qa2DsRiVyhW8yEuCoiK+RkQvFeDQgpEQFBqnEgVm4MTMmioYiomS+mQgzCMQjgzG0YkiIrgSs4cHIh3+X1BjpW2GD7G0K1GyLAUlGrwOynK2iIIEYUTkUHuLRkvG4nFYmCEDSkNVpzB4YUivDkYVeN0TCT9AmUf5wlGIgalwAQrBqKBNAhTh8lUCFEHobNsBuIgbgAdjMOwzCiCBuH+fJlCITRWysCREG2REIEcKxMkcSI5CMGoig8IRGFJVRumAOAHkQelDmAZEIH+bQwhojDlGQ-4ij0XAvmuV4iiEeY1huOAVk4JDRkiKRXhACQViEVR5kYbIqCsUh3QEBpuCKKwQBAUzVCIbAii+ZF8EYRxNG6cFRk4TJlAEbgVmvDxsigCRuA8XhQ2YZFImUGoQCoVx6F4DDJEyThsCsMRely9QX1Yeg0IEdgQrMVhyKgIR-isdjGC8IV5Fnbo4CZTkVnYRgpF4TReD0bgGgEG45FwEBSDgXBmEcUgwH+MAIVUPQpA8VhHSsBo7Aw3gQCMRxtFbSReAkIxkUQKxEVYL5IgoVRsCeMBcGyDwxDOdSdjsGQagkIgwkQVQzHobojHKBU4GUOREDCfI9GUDFlHmL4PDgIx6CEZhXnof5VWRYAoCkeg5FIUJslUV4rEwcttHoPRcioDDRkYeYIHmKRylwVgzBWbovnGVxNEibRcA8CRkzEeYGg8RBMmYDDgC7DxkdpCgcRqVgil4dganYKx2EyEBGqVKwMM0DgMLAbo5DsMIjGwMJvhk0ZmDGmpckYSJ8lYREjAKbBNBqN6jBAFZcE4MRXgoMwblGeZcjAXIlXmBPOG6XDlBATIBE4FZHCVCRXkyTBtA9yJcj0Kg9EicoUqgMQwgoMAQDCXJcnV3JHAECh6FwFYrEiZElX+DClTi0hEXKDD8nyf4IR6QkjDCTAQiVRxVG4LnMA8GvXm6AQBlGeQVjbA-XjvUZukybg7CkMx-iESYPAoRxumyJBV+4OaPDuoRgAwhADCVBRhmAoOUMINRMjlA8EeIgB5gCvHJBQbI7BNDgmYJoZQDRWB2DEPsbgRA4zaCQawX6vBED5AkKQbIcABB6EcB4dgNoVjAFUFgRECAlSuDsMHTgyIvhCHoBhL4tRsD5BuJEV4iJXDAG4MiLiqgsrdHYA0IQyJMCcFGKMEAnAN6RA0EIaQ9A2DcERNoPQqhVAwAAEQACo7FCAcQAHVwNoGQUBSCkBgI7MAmhgD4BgGBYAEAZCIBgIWRAAhcDAExDARgYQBDzBqDYmAZAIkAChwCICgK8YAYAwidVICEmJ8xoneOyJwCYRAYASFyfCV4GTsBgFeIgYA8xgDNNwOE+xdi6n73aZ4lx3MoD1BrLkmgESViIBkJM3gLcoCTIHCAAJqT0n9O4qmUgTSWltI6V0npDjlBIDACpLZpAHFrO8cc5AZzPHbIQMgEg5B4BEAye8sIwBIqfNwJ4xA6DYBCBgAAYjKa8V6iT2C5IBisKAKB-goFIAIT5cAMkAB4SDJIgK8AQ4c4AoGBdivxAA+d5UBumvE0KQZpHTAmcAAHQUAycCmA4EckwAEA0iJKAYBosqq8OA7BXhgGwCgKgnBiWuAEDABa8IYAzNgAE8CqZulLJgGHGs8BgA1mFfMcJnUkAyBgCRGA8wZAFLCOS+ogSZCgHldKsAMAIALI5dkU1HivH0pgFIV6AhWnhyQFAMImhJnKARDM+AAhPG4AAOThMQK8TE4SgVsvte68NaTcBwHVUmmVUBECLnlR4tJ2BHbtKQMAaJRakBFugjAfUYQwg+LCH4+twAZletcQACjEFAdtHjXiutrSAU1uLCkwGyAE7NjqiACBadmwAnGSAHg-gAlGizA-LBXCuwKSjJbLYCctybU+puSUBhsNTG7x2BclItwAU01iSVjZvIPvXJ7z3nYtxdgEFd7uiaBgKwUZ2qE0VqrcC9gtqckZNeLOswHKuUbImRswZpAUCAbGTkwdjrfnsBgJiXJ9KMlAoALTEuPa+14PKADaRhmASFcAAXRgJgfDrxXEAG9WN3pAIsnxZBSCbsmVAcAwAwgAF8Mmwfg0e5DWy0NAZ1aB3AGTEODtI+RzZnieWvAU5hrtXGFmTOaV4wTMBhNgFEyulT9SUMwHUzc05qd7k8ugIgKQL79Mvu41AKzDm7leLs2RuARAeUSDkF4bQrg8NeZdQ0HFI73M3tcXYTIchWByGi7k7zABeNFBmeOkv3TAS12RwkCDdX5pzXixWcH+DAL58rajkoyZV85dn1Oya04l14DnrMDK2YFmArXnPzB04G7rnmsuGb4yZv1cAhMibCL5k5-nvHqeCygc4YWMi8vy1AYlky0XGYE3Ng7ri0XmdE2R84rhmDepfdsqTh7B2ddQ0IaZMgMlgD1caQNGnbPAqIBQYLRAMJ9c02tsjw3qumrGzk97MzJvgoWct25VXIevM25INLUW0VgA+1KqCuADu7ZiwV1xN27sI8+9DtbHWbNybSQTxUzXXvtbI6pnl+OZmE+awan73Q-uvfB0e4j6mhAoAcVR5EnBOAMeGex7nMhee4Ak+S1F77P2FuBX6l60AYAeAENakFEH21QGg09hDJ7Xg0FeygPlc2hUivhRKw31Z6u4EzWZswwGAkQEy4OrtOmC3gu8dhqAuGlcq5XYRkj9OKPUdo-RpjmByBeMrbgDje2jP8c3RJy3MmGdaep19gXQui-eMB8DogoORdqY5xXlAiTrVI+85M4Fx3N12Ks0VvJoSytus5zAOxqgDWNv-QqmbJ2BWuO1TAc1pAPHZsSfMC1mhwe2fsyt9HKBXMTb26jxzbX1shbCxFqLe2YA4LmvdpLuAUtpYy3t3Le3CsCHZSVgfQ3t-nJq6MDlnuH8omLWP+9y7O-2cm3WvWbOW+aOv+o24adUH2reKOIBcBYBJ+WO22uOr+h2UeLOxOMAFOYgt2UyMyaBR+GB8eEOKA1O0BFe4BnOdBJyGS-ObSgu7Kwude4BEuUuMucuCuAebeTOPOBBMAYmNAGS6u76H6OK2u2KjInubuuK4S4GkG5uMGcw0mL21utujeDuAqTuoq-wruRu3iKhtaAeMAOm2AOm5A3SUA2aJqk++BROseg2Q+VG0MXgrgcgKeaexS0SrirhzWBeOh-WxeH2pe7B5eERleQOIOYOqmHhjezeKhXaIRuAVmMBUOoBMODSXymgGRzOROh+q2g2G2Z+cgkWw+V+rAN+SuRB9+qW6W9qRO2WmRe67+sAn+Q6tOf+ABE6FmYQFB5RYuDecRKAUBLBOR3+6BMOnUlq8OyBmRZR6OFRIW3hcgvhdRN+1OTRD+rRmRuWmRpKtOjBjeG+A24xGmp6rBCIZenBFe3BNxvBdi0usu8udiUhWaMhWu36wKOm2aHgwGMA8yPGlepuUGmhcGz2txNuduvAxoniaSxWoJ5WaSjaTqCypA7h1Bp6MAVGH4jGzGzqPGwRJRrO1uKRkxTq+a4JnixRohpRkmWhcJiJOJ0Rv2TxcRIK2AASNQrJsJVukxJebB3JsAwu32MRPJEOIKCRNeSRDBsBlB+RuShRTJyuBBaxx+QWp+4W1RUWdiuxjgZBRqyWLRGWUS7RnRRWvRGJ-R4q-+VaQBIx5xNxdu0xyAMh9x0pEpEBniXJHBkpDBHeApVxKJKpq2u+9JCyhkjkLShMpAXaOpGBepKAVRNRZJUAqgygkyUJRML6Yg02riAM32NQ3WxZPGky2ZVZsA1+ppDJ2y7p1BKGKAIJNYaK8Z4AlKb+H+UApWfReRqG3ZLSmgKAriYuri2ZuZriBZ3W3mpZLaySC5Cy05CydZEZdOExDS7ZwGXZKwCZvZvpjxIZcRLx4uku7x-BXxriRQEgzAYgBu6JbqpAh5PZSZPxGushX6fJKw7wlqMAJCiBYJLqahZuFubJIpDSkyduBhW6zuJhxKwFgaYerKEe2JPGeJO5BJNGdGJJmA2ZFJzJ5KHGtZCy+eUFheopH2MAQZsRcpVeiRLxO5bZSu5Keygamp0eW5g2-RBR2QRRqxox6xmBmZF+ZO9Z9Rpp+xFpj+IhWpNplJymdpA5X+jpWigxrpIlx+rZkBL6Qp2hAZqGoFEJJ5MpZ5EO9FspAO-JuAgpsx-RCBgaDJXa5FPGqZAWmBW2OOMAlOplKJ3aaK7l+2nl25xlu+CyuZvFHp1uLmUVyg5l-pwuyRrxV5HxAhdiHGmFOZ2aauvxmuchAJmgQakKMARQAgASAV8py5gpMJRlqmehkx8FRhLuxK9A6pk6KJMyNak+1KZ4XyhM2a2Z3uwGpAkyL0829WbqjqZSiSg6c+-VHSXVU1YAcAU4lhq+jaR66Sk+OmXwsK4y2ac1fqaak+2ZXqqgbq4Eo6819W3iS1g1jhlhrG91MANwlVvy06YenuAgRAL6DSXqrgtq3i+1h14Sc+PEwm2A3R3S2F8JieUgBFRFd6ylHGy8fiuS2gyKfqNZCVlFwp1FNBAV2yfpwZxlfJ4ZqVrFcmH1ASrlmRRmLaJVrw2N81eNPGuZVmYREVZpSV5NUpp5FNTFipMVuR8xqGAlQlylYVGxGZBpNRl+DZZpBxlpClKuJxylfZPRalQ5EtAxLpwxOlVBNNXWBlPNduJN-NDFKG1llltlVNyp4tqpJlrmrlM5ygK6dF7p6Z4ltRytTZMAhxGWHtuWHtZxw5FxkxDJXR-Zg5GJdu4qdWDWyAROzKxl4xnOH8YQ5AYtcxLtKAi4jyUA7tCVky2et1fqstmBUgRgygQgOxgdriYWUW85kldiL+klZ2zR8lodwVCVEdEtUdNBMV+JlG1tNlWyF5ZGbxGVXxX5fxRVIKRK-6YkCIWGpanyq+bSYGBZ7y9V7JuhcFm6rVSFa9LS3ik+jyYAzyUAXqgGIAh6YeWJrG41OVb9k16tBBuJRGNJu5hJWxvhKewWhlh9opcAKyym1NEVix+adUjaKZed-RsDOS3WyZYVpGG2rgrAUgYgugOx7gzUGWaDW5pGyDQacDTZiDtOmDIW2DuD+DGWhDXgVtr2ZDw5KAKDSBiO1d6Z9DeDDdTD7ELDJeLZptqGo9OF49qVl5fBnxDiC9rJiAUS-guy7SnSL0iA2yQAA)

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
