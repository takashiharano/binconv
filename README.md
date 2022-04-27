# binconv
Handy tool to convert between binary and text

## Building

```sh
mvn clean package
```

## Usage

```sh
java -jar binconv.jar -<MODE> [SRC] -i <SRC_FILE_PATH> -o <DEST_FILE_PATH> [-newline <NEWLINE_POS>]
```
MODE: frombase64|tobase64|fromhex|tohex|frombin|tobin
