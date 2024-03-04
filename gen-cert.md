scripts/register_connector.sh myedc

openssl x509 -in keys/myedc.cert -out keys/myedc.cert.pem  -outform PEM

openssl pkcs12 -export -in keys/myedc.cert.pem -inkey keys/myedc.key -out keys/myedc.cert.pfx

