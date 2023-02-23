package br.com.erudio.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class DozerMapper {

    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseList(List<O> originList, Class<D> destination) {
        var destinationList = new ArrayList<D>();
        for (O origin: originList) {
            destinationList.add(mapper.map(origin, destination));
        }
        return destinationList;
    }
}
