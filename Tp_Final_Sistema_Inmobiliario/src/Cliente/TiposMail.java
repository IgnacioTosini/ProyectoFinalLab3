package Cliente;

public enum TiposMail {
    /**
     * Tipos de subfijos de correos electronicos
     */

    Gmail("@gmail.com"), Hotmail("@hotmail.com"), Yahoo("@yahoo.com");
    private String tipomail;

    TiposMail(String tipomail) {
        this.tipomail = tipomail;
    }

    public String getTipomail() {
        return tipomail;
    }
}
