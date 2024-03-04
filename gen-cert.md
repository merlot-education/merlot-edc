scripts/register_connector.sh myedc

openssl pkcs12 -export -in keys/myedc.cert.pem -inkey keys/myedc.key -out keys/myedc.cert.pfx

