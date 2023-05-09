function msg = diagPulseDiff(ecg)

    %% Calcula la distancia entre ondas R para calcular la frecuencia cardíaca
    %   Taquicardía: > 100 lpm
    %   Bradicardia: < 60 lpm

    [~, pos] = findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 100);   % Para sacar la onda R
    
    % Para visualizar *******************************************************************
    %findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 100);

    distancias = (pos(2:end) - pos(1:end-1)) / 10;
    
    if std(distancias) > 1  %   Ritmo irregular
        msg = "Ritmo irregular, ";
        frec = length(pos) * 6;     %   NOTA: para este método es necesaria todos los complejos QRS del ecg, no sólo unos pocos
    else                    %   Ritmo regular
        msg = "Ritmo regular, ";
        frec = round(1500 / mean(distancias), 2);        %   Para calcular la frecuencia se divide 1500 / la media
    end


    if frec > 100
        msg = msg + "posible taquicardia debido a frecuencia cardíaca de " + frec + ".";
    elseif frec < 60
        msg = msg + "posible bradicardia debido a frecuencia cardíaca de " + frec + ".";
    else
        msg = msg + "latidos por minuto normal (" + frec + " lpm).";
    end

end

