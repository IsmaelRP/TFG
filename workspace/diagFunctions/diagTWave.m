function msg = diagTWave(ecg)
    %% Calcula las dimensiones de la onda T para determinar parámetros correctos
    %   Problema de 


    moda = mode(ecg);

    [~, posR, anchuraR] = findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 120);

    % Para visualizar ************************************************************************************
    subplot(4, 1, 1);
    findpeaks(ecg, 'MinPeakProminence', 20, 'MinPeakHeight', 60, 'MinPeakDistance', 120);
    title("Señal original");
    hold on;

    for i=length(posR):-1:1
        if i > 1
            limpiarHasta = round((posR(i) - posR(i-1)) / 2);
        else
            limpiarHasta = posR(i)-1;
        end

        ecg( posR(i) - limpiarHasta : posR(i) + round(anchuraR(i)) + 3 ) = moda - 5;
        %plot(ecg, 'r');

    end
    subplot(4, 1, 2);
    plot(ecg, 'r');
    title("Aislado de ondas T");
    
    [picosT, posT, anchuraT] = findpeaks(smoothdata(ecg, 'movmean', 25), 'MinPeakDistance', 100, 'MaxPeakWidth', 40, 'MinPeakProminence', 5);

    % Para visualizar ****************************************
    subplot(4, 1, 3);
    findpeaks(smoothdata(ecg, 'movmean', 25), 'MinPeakDistance', 100, 'MaxPeakWidth', 40, 'MinPeakProminence', 5);
    title("Suavizado y selección de ondas T");

    if length(posT) == length(posR)     % Posible onda T alta
        
        desvInicio = 0;
        desvFin = 10;
        alturaT = size(1, length(posT));
        for i=1:length(posT)
            cambioDireccion = ischange( ecg(floor(posT(i) - anchuraT(i) -desvInicio): min([floor(posT(i) + anchuraT(i) +desvFin), length(ecg)]) ) , 'linear', 'Threshold', 2);
    
            inicioProminencia = find(cambioDireccion == 1, 1, 'first') + floor(posT(i) - anchuraT(i) -desvInicio) -2;
            finProminencia = find(cambioDireccion == 1, 1, 'last') + floor(posT(i) - anchuraT(i) );
    
            alturaT(i) = picosT(i) - mean([ ecg(inicioProminencia), ecg(finProminencia) ]);
        end

        alturaT = alturaT / 10;
        if mean(alturaT > 3.3) > 0.6
            msg = "Onda T alta-dentada, posible hipercalemia o infarto agudo de miocardio";
        else
            msg = "Onda T normal (repolarización ventricular correcta)";
        end

    elseif length(posT) < length(posR)     % Posible onda T plana o invertida
        nMonticulos = length(posT);

        ecg = gnegate(ecg);     %   Flipeamos la señal para detectar las invertidas

        picosInvertidos = findpeaks(ecg, 'MinPeakDistance', 100, 'MaxPeakWidth', 60, 'MinPeakProminence', 10);

        % Para visualizar ****************************************
        subplot(4, 1, 4);
        findpeaks(ecg, 'MinPeakDistance', 100, 'MaxPeakWidth', 60, 'MinPeakProminence', 10);
        title("Inversa de la señal para diferenciar entre plana o inversa")

        if length(picosInvertidos) >= length(posR) * 0.6
            msg = "Onda T invertida, posible isquemia, embolia pulmonar, miocardiopatía hipertrófica o enfermedad general";
        elseif nMonticulos < length(posR) * 0.5
            msg = "Onda T plana, posible isquemia o desequilibrio electrolítico";
        else
            msg = "Onda T normal (repolarización ventricular correcta)";
        end

    else
        msg = "Onda T normal (repolarización ventricular correcta)";
    end
    
    %hold off;

end

