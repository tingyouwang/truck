package com.luzhu.truck.util;

public class RsaKeyPair {

    private String privateKey;
    private String publicKey;
    private String modulus;

    public RsaKeyPair() {
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public String getModulus() {
        return this.modulus;
    }

    public void setPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }

    public void setModulus(final String modulus) {
        this.modulus = modulus;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RsaKeyPair)) {
            return false;
        } else {
            RsaKeyPair other = (RsaKeyPair)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$privateKey = this.getPrivateKey();
                    Object other$privateKey = other.getPrivateKey();
                    if (this$privateKey == null) {
                        if (other$privateKey == null) {
                            break label47;
                        }
                    } else if (this$privateKey.equals(other$privateKey)) {
                        break label47;
                    }

                    return false;
                }

                Object this$publicKey = this.getPublicKey();
                Object other$publicKey = other.getPublicKey();
                if (this$publicKey == null) {
                    if (other$publicKey != null) {
                        return false;
                    }
                } else if (!this$publicKey.equals(other$publicKey)) {
                    return false;
                }

                Object this$modulus = this.getModulus();
                Object other$modulus = other.getModulus();
                if (this$modulus == null) {
                    if (other$modulus != null) {
                        return false;
                    }
                } else if (!this$modulus.equals(other$modulus)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RsaKeyPair;
    }

    public int hashCode() {
//        int PRIME = true;
        int result = 1;
        Object $privateKey = this.getPrivateKey();
        result = result * 59 + ($privateKey == null ? 43 : $privateKey.hashCode());
        Object $publicKey = this.getPublicKey();
        result = result * 59 + ($publicKey == null ? 43 : $publicKey.hashCode());
        Object $modulus = this.getModulus();
        result = result * 59 + ($modulus == null ? 43 : $modulus.hashCode());
        return result;
    }

    public String toString() {
        String var10000 = this.getPrivateKey();
        return "RsaKeyPair(privateKey=" + var10000 + ", publicKey=" + this.getPublicKey() + ", modulus=" + this.getModulus() + ")";
    }
}
