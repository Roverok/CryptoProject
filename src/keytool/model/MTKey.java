package keytool.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.x509.X509V3CertificateGenerator;


public class MTKey {
	private Key key;
	private Certificate certificate;
	
	public MTKey(String subject) throws CertificateEncodingException, InvalidKeyException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, ModelException {
		generateNewKey(subject);
	}
	
	public MTKey(Key key2) {
		this.key = key2;
	}

	public Key getKey() {
		return this.key;
	}

	/**
	 * Warning : there is no certificate in a Key from a Keystore !
	 * You have to get the Certificate from the KeyStore
	 * @return
	 */
	public Certificate getCertificate() {
		return this.certificate;
	}
	
	public String toString() {
		return "clé "+key.getAlgorithm()+", "+key.getFormat()+", "+key.getClass().getName();
	}
	
	public String getDetails() {
		return this.key.getClass().getName();
	}
	
	public void generateNewKey(String subject) throws CertificateEncodingException, InvalidKeyException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, ModelException  {

		/* Génération d'une paire de clé privée/publique */
	    KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");
	    kpGen.initialize(1024, new SecureRandom());
	    KeyPair keyPair = kpGen.generateKeyPair();
	    /* Save the key */
	    this.key = keyPair.getPrivate();
	    
	    /* Génération d'un certificat qui encapsule la paire de clé */
	    X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

	    SecureRandom random = new SecureRandom();

	    certGen.setSerialNumber(BigInteger.valueOf(random.nextInt(random.nextInt(Integer.MAX_VALUE))));
	    certGen.setIssuerDN(new X500Principal(subject));
	    certGen.setNotBefore(new Date(System.currentTimeMillis() - 10000));
	    certGen.setNotAfter(new Date(System.currentTimeMillis() + 10000));
	    certGen.setSubjectDN(new X500Principal(subject));
	    certGen.setPublicKey(keyPair.getPublic());
	    certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

	    /* Sauvegarde du certificat */
	    this.certificate = certGen.generate(keyPair.getPrivate(), "BC");
	}
	
	/**
	 * Export the PrivateKey to Base64 String
	 * if you wan the publicKey, go through the certificate
	 * @return
	 * @throws CertificateEncodingException
	 * @throws IOException
	 */
	public String toBase64() throws CertificateEncodingException, IOException {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    
		PEMWriter pemWrt = new PEMWriter(new OutputStreamWriter(bOut));
    
		pemWrt.writeObject(this.key);
		pemWrt.close();
		bOut.close();
    
		return bOut.toString();
	}
}
