package keytool.model;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.x509.X509V3CertificateGenerator;

/**
 * Certificate encapsulation
 * @author Michaël Muré & Théophile Helleboid
 *
 */
public class MTCertificate {
	private Certificate certificate;
	private static String DEFAULT_SUBJECT = "CN=Alice-Subject, O=ENSISA, L=Mulhouse, ST=68, C=FR";
	private static String DEFAULT_ISSUER = "CN=Bob-Issuer, O=DataPower, L=Cambridge, ST=MA, C=US";


	/**
	 * Certificate constructor with a Java Certificate
	 * @param certificate to encapsulate
	 */
	public MTCertificate(Certificate certificate) {
		this.certificate = certificate;
	}
	
	/**
	 * Default constructor with a default Certificate
	 * @throws CertificateEncodingException
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 */
	public MTCertificate() throws CertificateEncodingException, InvalidKeyException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException {
		this(DEFAULT_SUBJECT, DEFAULT_ISSUER);
	}

	/**
	 * Constructor of MTCertificate with the subject
	 * @param subject
	 * @throws CertificateEncodingException
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 */
	public MTCertificate(String subject) throws CertificateEncodingException, InvalidKeyException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException {
		this(subject, subject);
	}
	
	/**
	 * MTCertificate for importing a Certificate from a file
	 * @param fis : FileInputStream Certificate to import
	 * @throws ModelException
	 */
	public MTCertificate(FileInputStream fis) throws ModelException {
		try {
			// Le flot transmis à la méthode generateCertificate() doit supporter
			// les opérations mark() et reset() ce qui est le cas de BufferedInputStream
			// mais pas celui de FileInputStream.
			InputStream in = new BufferedInputStream(fis);
			// l'usine est spécialisée dans le traitement des certificats X509.
			CertificateFactory factory = CertificateFactory.getInstance("X509");
			// Comme les lectures ne sont pas faites explicitement c'est la méthode available()
			// qui permettra de savoir si la fin de fichier est atteinte
	
			if(in.available() > 0) {
				this.certificate = factory.generateCertificate(in);
			} else
				throw new CertificateException("Pas de certificat dans le fichier !");
		} catch (CertificateException e) {
			throw new ModelException("Problème de certificat : "+e.getMessage());

		} catch (IOException e) {
			throw new ModelException("Problème d'entrée/sortie : "+e.getMessage());

		}

	}
	
	/**
	 * Constructor for Certificate with a subject and an issuer
	 * @param subject
	 * @param issuer
	 * @throws CertificateEncodingException
	 * @throws InvalidKeyException
	 * @throws IllegalStateException
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 */
	public MTCertificate(String subject, String issuer) throws CertificateEncodingException, InvalidKeyException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException {

		/* Génération d'une paire de clé privée/publique */
	    KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");
	    kpGen.initialize(1024, new SecureRandom());
	    KeyPair keyPair = kpGen.generateKeyPair();

	    
	    /* Génération d'un certificat qui encapsule la paire de clé */
	    X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

	    certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
	    certGen.setIssuerDN(new X500Principal(issuer));
	    // One day before now
	    certGen.setNotBefore(new Date(System.currentTimeMillis() - (60 * 60 * 24)* 1000 ));
	    // One year after today
	    certGen.setNotAfter(new Date(System.currentTimeMillis() + (365 * 60 * 60 * 24)* 1000 ));
	    certGen.setSubjectDN(new X500Principal(subject));
	    certGen.setPublicKey(keyPair.getPublic());
	    certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

	    /* Sauvegarde du certificat */
	    this.certificate = certGen.generate(keyPair.getPrivate(), "BC");
	}
	
	/**
	 * Provide details about the certificate
	 * @return String with details
	 */
	public String getDetails() {
		X509Certificate cert = (X509Certificate) this.certificate;
		StringBuilder sb = new StringBuilder();
		if(cert != null) {
			sb.append("Émetteur:\n  ").append(cert.getIssuerDN().toString());
			sb.append("\nPropriétaire:\n  ").append(cert.getSubjectDN().toString());
			sb.append("\nValide pas avant:\n  ").append(cert.getNotBefore().toString());
			sb.append("\nValide pas après:\n  ").append(cert.getNotAfter().toString());
			sb.append("\nNuméro de série:\n  ").append(cert.getSerialNumber());
			sb.append("\nAlgorithme de signature:\n  ").append(cert.getSigAlgName());
			sb.append("\nType:\n  ").append(cert.getType());
		} else {
			sb.append("Pas de certificat X509");
		}
		return sb.toString();
	}
	
	/**
	 * add the current certificate to a given keystore
	 * @param keystore 's target
	 * @param alias for the certificate in the KS
	 * @throws ModelException
	 */
	public void addToKeyStore(Model keystore, String alias) throws ModelException {
		keystore.addCertificate(alias, this.certificate);
	}
	
	/**
	 * Return details of the certificate
	 */
	public String toString() {
		return getDetails();
	}

	/**
	 * Return base64 encoding of the certificate
	 * @return base64 string encoding
	 * @throws CertificateEncodingException
	 * @throws IOException
	 */
	public String toBase64() throws CertificateEncodingException, IOException {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    
		PEMWriter pemWrt = new PEMWriter(new OutputStreamWriter(bOut));
    
		pemWrt.writeObject(this.certificate);
		pemWrt.close();
		bOut.close();
    
		return bOut.toString();
	}
	
	/**
	 * Export the certificate to a path
	 * @param path to export the certificate
	 * @throws ModelException
	 */
	public void exportTo(String path) throws ModelException {
		try {
			BufferedWriter buf = new BufferedWriter(new FileWriter(path));
			buf.write(this.toBase64());
			buf.close();
		} catch (CertificateEncodingException e) {
			throw new ModelException("Problème d'encodage de certificat : "+e.getMessage());
		} catch (IOException e) {
			throw new ModelException("Problème d'entrée/sortie : "+e.getMessage());
		}
		
	}
}
