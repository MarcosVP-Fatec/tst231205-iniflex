package br.com.mvp.maonamassa.model.service;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
public class Util {

    // Data no formato String DD/MM/AAAA
    public static String dToStr(LocalDate data) {
        return (data.getDayOfMonth() < 10 ? "0" : "") + String.valueOf(data.getDayOfMonth())
                + "/" + (data.getMonthValue() < 10 ? "0" : "") + String.valueOf(data.getMonthValue())
                + "/" + String.valueOf(data.getYear());
    }

    public static void geraTxt(String texto, String nomeArq) throws IOException {
        Path sub = Paths.get("spool");
        if (!Files.exists(sub))
            Files.createDirectories(sub);

        nomeArq = sub.toString() + "/" + nomeArq;

        FileWriter handle = new FileWriter(nomeArq);
        handle.write(texto);
        handle.close();
        System.out.println("====================================================");
        System.out.println(texto);
        System.out.println("====================================================");
    }

    // Corta o texto à  esquerda conforme tamanho informado
    public static String left(String texto, int nTam) {
        return texto.substring(0, texto.length() < nTam ? texto.length() : nTam);
    }

    // Preenche o texto à direita com espaços conforme tamanho informado
    public static String padR(String texto, int nTam) {
        if (texto.length() >= nTam)
            return left(texto, nTam);
        return texto + " ".repeat(nTam - texto.length());
    }

    // Preenche o texto à direita com espaços conforme tamanho informado
    public static String padL(String texto, int nTam) {
        if (texto.length() >= nTam)
            return texto;
        return " ".repeat(nTam - texto.length()) + texto;
    }

    // Gera uma string com o valor exibido conforme a máscara informada ###,###.##
    public static String maskDec(BigDecimal valor, int tam) {
        final DecimalFormat MASCARA = new DecimalFormat("¤ ###,##0.00",
                new DecimalFormatSymbols(new Locale("pt", "BR")));
        String ret = MASCARA.format(valor).trim();
        if (ret.length() < tam) {
            ret = left(ret, 3) + " ".repeat(tam - ret.length()) + right(ret, ret.length() - 3);
        }
        return ret;
    }

    // Extrai parte à direita do texto conforme tamanho informado
    public static String right(String texto, int nTam) {
        return texto.substring(texto.length() <= nTam ? 0 : texto.length() - nTam);
    }

}
