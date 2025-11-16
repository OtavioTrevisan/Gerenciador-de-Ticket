package br.com.ticket.view.util;

import javax.swing.text.*;

public class FiltroNumerico extends DocumentFilter {

    /*Método de filtro numérico, usado para impedir que caracteres que não sejam números sejam adicionados
    * */
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {

        if (text != null && text.matches("[0-9]+")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {

        if (string != null && string.matches("[0-9]+")) {
            super.insertString(fb, offset, string, attr);
        }
    }
}
