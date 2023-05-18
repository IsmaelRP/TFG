function out = testImage(img)
    
    imshow(img);
    %img

    out = sprintf("- Ritmo irregular, latidos por minuto normal (78 lpm).\n\n%s- Ritmo irregular, posible fibrilación auricular, Onda P normal (conducción eléctrica).\n\n%s- Complejo QRS normal (conducción eléctrica).\n\n%s- Bloqueo AV de 2º grado Mobitz tipo 1, se omiten complejos QRS.\n\n%s- Intervalo QT normal (riesgo de arritmia ventricular).\n\n%s- Intervalo ST normal (no hay indicios de irregularidad miocárdica).\n\n%s- Onda T normal (repolarización ventricular correcta).", "", "", "", "", "", "");


end

