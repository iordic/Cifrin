# Cifrín
Pequeña aplicación con GUI muy simple realizada para una de las prácticas de *Criptografía y Seguridad Informática*.

## Funciones
La aplicación está pensada para trabajar sobre ficheros de texto plano. De modo que podamos ver el resultado de forma didáctica. Aunque técnicamente el cifrado se realiza sobre los bytes de este. Las funciones que realiza son:
* Cifrado y descifrado con los algoritmos:
  * DES
  * Triple DES
  * AES
  * RSA
* Generación de claves para los algoritmos mencionados.
* Generación de hash en los siguientes algoritmos:
  * MD5
  * SHA1
  * SHA256
  * SHA512
* Crear y editar ficheros de texto plano sobre los que realizaremos el cifrado.

## Dependencias
Para realizar todas las funciones de cifrado, descifrado y generación de hash se ha utilizado la librería de [BouncyCastle](https://www.bouncycastle.org/latest_releases.html). Librería que viene incluída en la carpeta *lib* de este mismo proyecto y que debemos importar para que funcione.

#### Tamaño de las claves de cifrado
Se puede cambiar el tamaño de las claves en los ficheros de Java pertinentes, dentro del paquete *crypto* (no implementado en la GUI).
