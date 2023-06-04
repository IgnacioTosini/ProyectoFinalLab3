package Cliente;

public enum TiposMail {

    Gmail("@gmail.com"), Hotmail("@hotmail.com"), Yahoo("@yahoo.com");
    private String tipomail;
    private TiposMail(String tipomail) {
    this.tipomail = tipomail;
    }

    public String getTipomail() {
        return tipomail;
    }
}
