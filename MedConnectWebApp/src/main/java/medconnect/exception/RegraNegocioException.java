package medconnect.exception;

/** Violação de regra de negócio ou validação (HTTP 400). */
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) { super(mensagem); }
}
