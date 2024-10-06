package com.example.assignment.coordinationapi.configuration;

import com.example.assignment.common.model.entity.Brand;
import com.example.assignment.common.model.entity.ClothCategory;
import com.example.assignment.common.model.enums.ClothType;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AssignmentData {

    public static final Map<String, Brand> BRAND_DATA = Map.ofEntries(
            Map.entry("A", new Brand("A")),
            Map.entry("B", new Brand("B")),
            Map.entry("C", new Brand("C")),
            Map.entry("D", new Brand("D")),
            Map.entry("E", new Brand("E")),
            Map.entry("F", new Brand("F")),
            Map.entry("G", new Brand("G")),
            Map.entry("H", new Brand("H")),
            Map.entry("I", new Brand("I"))
    );

    public static final Map<ClothType, ClothCategory> CATEGORY_DATA = Map.ofEntries(
            Map.entry(ClothType.TOP, new ClothCategory(10000, 1, ClothType.TOP, ClothType.TOP.getName())),
            Map.entry(ClothType.OUTER, new ClothCategory(10001, 1, ClothType.OUTER, ClothType.OUTER.getName())),
            Map.entry(ClothType.BOTTOM, new ClothCategory(20000, 2, ClothType.BOTTOM, ClothType.BOTTOM.getName())),
            Map.entry(ClothType.PANTS, new ClothCategory(20001, 2, ClothType.PANTS, ClothType.PANTS.getName())),
            Map.entry(ClothType.SHOES, new ClothCategory(30000, 3, ClothType.SHOES, ClothType.SHOES.getName())),
            Map.entry(ClothType.SNEAKERS, new ClothCategory(30001, 3, ClothType.SNEAKERS, ClothType.SNEAKERS.getName())),
            Map.entry(ClothType.BAG, new ClothCategory(40000, 4, ClothType.BAG, ClothType.BAG.getName())),
            Map.entry(ClothType.HAT, new ClothCategory(50000, 5, ClothType.HAT, ClothType.HAT.getName())),
            Map.entry(ClothType.SOCKS, new ClothCategory(60000, 6, ClothType.SOCKS, ClothType.SOCKS.getName())),
            Map.entry(ClothType.ACCESSORY, new ClothCategory(70000, 7, ClothType.ACCESSORY, ClothType.ACCESSORY.getName()))
    );

    public static final List<ClothType> REQUIRED_CATEGORY = List.of(ClothType.TOP, ClothType.OUTER, ClothType.PANTS, ClothType.SNEAKERS, ClothType.BAG, ClothType.HAT, ClothType.SOCKS, ClothType.ACCESSORY);

    public static final Map<String, List<Pair<ClothType, BigDecimal>>> SALES_DATA = Map.ofEntries(
            Map.entry("A", List.of(
                    Pair.of(ClothType.TOP, new BigDecimal(11_200)),
                    Pair.of(ClothType.OUTER, new BigDecimal(5_500)),
                    Pair.of(ClothType.PANTS, new BigDecimal(4_200)),
                    Pair.of(ClothType.SNEAKERS, new BigDecimal(9_000)),
                    Pair.of(ClothType.BAG, new BigDecimal(2_000)),
                    Pair.of(ClothType.HAT, new BigDecimal(1_700)),
                    Pair.of(ClothType.SOCKS, new BigDecimal(1_800)),
                    Pair.of(ClothType.ACCESSORY, new BigDecimal(2_300))
            )),
            Map.entry("B", List.of(
                    Pair.of(ClothType.TOP, new BigDecimal(10_500)),
                    Pair.of(ClothType.OUTER, new BigDecimal(5_900)),
                    Pair.of(ClothType.PANTS, new BigDecimal(3_800)),
                    Pair.of(ClothType.SNEAKERS, new BigDecimal(9_100)),
                    Pair.of(ClothType.BAG, new BigDecimal(2_100)),
                    Pair.of(ClothType.HAT, new BigDecimal(2_000)),
                    Pair.of(ClothType.SOCKS, new BigDecimal(2_000)),
                    Pair.of(ClothType.ACCESSORY, new BigDecimal(2_200))
            )),
            Map.entry("C", List.of(
                    Pair.of(ClothType.TOP, new BigDecimal(10_000)),
                    Pair.of(ClothType.OUTER, new BigDecimal(6_200)),
                    Pair.of(ClothType.PANTS, new BigDecimal(3_300)),
                    Pair.of(ClothType.SNEAKERS, new BigDecimal(9_200)),
                    Pair.of(ClothType.BAG, new BigDecimal(2_200)),
                    Pair.of(ClothType.HAT, new BigDecimal(1_900)),
                    Pair.of(ClothType.SOCKS, new BigDecimal(2_200)),
                    Pair.of(ClothType.ACCESSORY, new BigDecimal(2_100))
            )),
            Map.entry("D", List.of(
                    Pair.of(ClothType.TOP, new BigDecimal(10_100)),
                    Pair.of(ClothType.OUTER, new BigDecimal(5_100)),
                    Pair.of(ClothType.PANTS, new BigDecimal(3_000)),
                    Pair.of(ClothType.SNEAKERS, new BigDecimal(9_500)),
                    Pair.of(ClothType.BAG, new BigDecimal(2_500)),
                    Pair.of(ClothType.HAT, new BigDecimal(1_500)),
                    Pair.of(ClothType.SOCKS, new BigDecimal(2_400)),
                    Pair.of(ClothType.ACCESSORY, new BigDecimal(2_000))
            )),
            Map.entry("E", List.of(
                    Pair.of(ClothType.TOP, new BigDecimal(10_700)),
                    Pair.of(ClothType.OUTER, new BigDecimal(5_000)),
                    Pair.of(ClothType.PANTS, new BigDecimal(3_800)),
                    Pair.of(ClothType.SNEAKERS, new BigDecimal(9_900)),
                    Pair.of(ClothType.BAG, new BigDecimal(2_300)),
                    Pair.of(ClothType.HAT, new BigDecimal(1_800)),
                    Pair.of(ClothType.SOCKS, new BigDecimal(2_100)),
                    Pair.of(ClothType.ACCESSORY, new BigDecimal(2_100))
            )),
            Map.entry("F", List.of(
                    Pair.of(ClothType.TOP, new BigDecimal(11_200)),
                    Pair.of(ClothType.OUTER, new BigDecimal(7_200)),
                    Pair.of(ClothType.PANTS, new BigDecimal(4_000)),
                    Pair.of(ClothType.SNEAKERS, new BigDecimal(9_300)),
                    Pair.of(ClothType.BAG, new BigDecimal(2_100)),
                    Pair.of(ClothType.HAT, new BigDecimal(1_600)),
                    Pair.of(ClothType.SOCKS, new BigDecimal(2_300)),
                    Pair.of(ClothType.ACCESSORY, new BigDecimal(1_900))
            )),
            Map.entry("G", List.of(
                    Pair.of(ClothType.TOP, new BigDecimal(10_500)),
                    Pair.of(ClothType.OUTER, new BigDecimal(5_800)),
                    Pair.of(ClothType.PANTS, new BigDecimal(3_900)),
                    Pair.of(ClothType.SNEAKERS, new BigDecimal(9_000)),
                    Pair.of(ClothType.BAG, new BigDecimal(2_200)),
                    Pair.of(ClothType.HAT, new BigDecimal(1_700)),
                    Pair.of(ClothType.SOCKS, new BigDecimal(2_100)),
                    Pair.of(ClothType.ACCESSORY, new BigDecimal(2_000))
            )),
            Map.entry("H", List.of(
                    Pair.of(ClothType.TOP, new BigDecimal(10_800)),
                    Pair.of(ClothType.OUTER, new BigDecimal(6_300)),
                    Pair.of(ClothType.PANTS, new BigDecimal(3_100)),
                    Pair.of(ClothType.SNEAKERS, new BigDecimal(9_700)),
                    Pair.of(ClothType.BAG, new BigDecimal(2_100)),
                    Pair.of(ClothType.HAT, new BigDecimal(1_600)),
                    Pair.of(ClothType.SOCKS, new BigDecimal(2_000)),
                    Pair.of(ClothType.ACCESSORY, new BigDecimal(2_000))
            )),
            Map.entry("I", List.of(
                    Pair.of(ClothType.TOP, new BigDecimal(11_400)),
                    Pair.of(ClothType.OUTER, new BigDecimal(6_700)),
                    Pair.of(ClothType.PANTS, new BigDecimal(3_200)),
                    Pair.of(ClothType.SNEAKERS, new BigDecimal(9_500)),
                    Pair.of(ClothType.BAG, new BigDecimal(2_400)),
                    Pair.of(ClothType.HAT, new BigDecimal(1_700)),
                    Pair.of(ClothType.SOCKS, new BigDecimal(1_700)),
                    Pair.of(ClothType.ACCESSORY, new BigDecimal(2_400))
            ))
    );

}
