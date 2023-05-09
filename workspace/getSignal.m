function vector = getSignal(im2)
    
    im3 = im2(:,:,1);        % Nos quedamos con el canal rojo para eliminar la rejilla 
    im4 = uint8(im3*2.55);   % Ajustamos el contraste de la imagen para que la parte negra sea únicamente la señal
    im5 = 255-im4;           % Invertimos la imagen
    im6 = imadjust(im5);     % Normalizamos la imagen

    linea = bwskel(imbinarize(im6));         % Binarizamos la imagen y calculamos el esqueleto (bwskel)
    
    vector = arraySignalToVector(linea);     % Pasamos de 3D a 1D
    vector = smoothdata(vector);             % Suavizamos la curva por posibles distorsiones en los píxeles


end

