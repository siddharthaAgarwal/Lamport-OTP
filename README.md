# Lamport-OTP

Lamport One Time Signature is a method for constructing a digital signature and typically
involves the use of a cryptographic hash function. As it is a one-time signature scheme, it
can only be used to securely sign one message.

Suppose Alice wants to digitally sign her message to Bob. The process of the Algorithm
consists of 3 steps -
● Generating key pair ( a private key and a public key)
● Generating signature at Alice’s side.
● Verifying signature at Bob’s side for Authorizing.


## Algorithm Explanation:

### Making the key pair
● To create the private key Alice uses the random number generator to produce 256 pairs
of random numbers (2×256 numbers in total), each number being 256 bits in size.
This private key is available to Alice only. She can use it to sign messages.
● Public key creation is done by hashing each of these numbers using a 256-bit
cryptographic hash function

### Signing the message
● First, Alice hashes her message using a 256-bit cryptographic hash function, eg. SHA256
to obtain a 256-bit digest.
● In case of an enhanced scheme. She can add salt to the message to create a digest.
● For each bit in digest(0 or 1) Alice will choose the corresponding row from the private key
and generate a 256 bit signature. If bit = 0, first row’s column is chosen If bit = 1, second
row’s column is chosen.
● Since Alice's private key is used, it should never be used again. She must destroy the
other 256 numbers that she did not use for the signature.

### Verifying the signature
● Bob hashes the message using the same 256-bit cryptographic hash function, to
obtain a 256-bit digest.
● In case of an enhanced scheme. He can add salt to the message to create a digest.
● For each bit in digest(0 or 1) Bob will choose the corresponding row from the public key
(shared to all) and generate a 256 bit signature. If bit = 0, first row’s column is chosen If
bit = 1, second row’s column is chosen.
● Bob hashes each of the numbers in Alice’s signature to obtain a 256-bit digest.
● Now, both the digests are compared bit by bit. If there is a mismatch signature is not
valid, else valid.
