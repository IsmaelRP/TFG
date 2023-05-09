function y = arraySignalToVector(x)     %   Convierte una matriz de señales en un vector con su valor correspondiente

    dimen = size(x, 2);
    y = zeros(1, dimen);
    
    for i=1:dimen
        
        aux = x(:, i);

        valor = find(aux > 0);
        if isempty(valor)
            y(i) = 0;               %   En caso que no haya señal en una columna dada
        else
            y(i) = round(mean(valor));      %   En caso de que haya varios números en una columna, hago la media
        end
    end
    
    y = size(x, 1) - y;

end

