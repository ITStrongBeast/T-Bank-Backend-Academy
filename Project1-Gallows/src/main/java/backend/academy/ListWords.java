package backend.academy;

import java.util.Map;
import lombok.Getter;

@Getter
public enum ListWords {
    ANIMALS(Map.ofEntries(
        Map.entry("cat", "Домашнее ласковое пушистое животное"),
        Map.entry("dog", "Домашнее животное, которое надо выгуливать"),
        Map.entry("parrot", "Красивая домашняя птица"),
        Map.entry("cow", "Домашнее животное используемое в сельском хозяйстве"),
        Map.entry("horse", "Домашнее животное используемое для верховой езды"),
        Map.entry("hamster", "Маленькое пушистое домашнее животное"),
        Map.entry("giraffe", "Животное с длинной шеей"),
        Map.entry("lion", "Дикое животное из семейства кошачьих"),
        Map.entry("bear", "Дикое животное, которое любит мёд"),
        Map.entry("cheetah", "Самое быстрое животное на суше")
    )),

    CARS(Map.ofEntries(
        Map.entry("bmw", "Ест много масла и часто ломается"),
        Map.entry("audi", "Эмблема - четыре металлических кольца"),
        Map.entry("mercedes", "Германский бренд премиального класса"),
        Map.entry("lada", "Знаменитый российский бренд"),
        Map.entry("toyota", "Camry 3.5"),
        Map.entry("lamborghini", "Итальянский бренд который начинал с производства тракторов"),
        Map.entry("subaru", "На эмблеме изображено звёздное скопление Плеяд"),
        Map.entry("kia", "Популярная южнокорейская автомобилестроительная компания"),
        Map.entry("mitsubishi", "Моторы этой компании японцы использовали в самолётах на войне"),
        Map.entry("ford", "Основатель компании одним из первых внедрил конвейер в свои заводы"),
        Map.entry("rolls-royce", "Выпускает под заказ автомобили класса люкс"),
        Map.entry("peugeot", "Самая древняя марка автомобилей"),
        Map.entry("bugatti", "компания специализируется на производстве быстрых спортивных автомобилей")
    )),

    TEMPLATES(Map.ofEntries(
        Map.entry("factory", "Создание объектов без указания их конкретных классов"),
        Map.entry("builder", "Пошаговое создание сложных объектов"),
        Map.entry("prototype", "Создание копий объектов с минимальными затратами"),
        Map.entry("singleton", "Один экземпляр класса на приложение"),
        Map.entry("decorator", "Динамическое добавление функциональности объекту"),
        Map.entry("facade", "Упрощение интерфейса для сложной системы"),
        Map.entry("iterator", "Последовательный доступ к элементам коллекции"),
        Map.entry("bridge", "Разделение абстракции и реализации"),
        Map.entry("observer", "Реакция на изменения объекта другими объектами"),
        Map.entry("visitor", "Добавление операций без изменения классов")
    )),

    PARADIGMS(Map.ofEntries(
        Map.entry("functional", "Функции как основные строительные блоки"),
        Map.entry("imperative", "Указание шагов для выполнения задач"),
        Map.entry("object-oriented", "Моделирование данных и поведения объектов"),
        Map.entry("procedural", "Разделение программы на процедуры и функции"),
        Map.entry("declarative", "Описание того, что нужно сделать, а не как"),
        Map.entry("logical", "Решение проблем через логические правила"),
        Map.entry("reactive", "Реакция на события и изменения данных"),
        Map.entry("structural", "Организация и композиция программных компонентов")
    ));

    private final Map<String, String> hint;

    ListWords(Map<String, String> hint) {
        this.hint = hint;
    }

    public Map<String, String> getHint() {
        return hint;
    }
}
