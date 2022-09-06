package task2Streams;

import java.util.*;
import java.util.stream.Collectors;

public class ComplexExamples {

    static class Person {
        final int id;

        final String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person person)) return false;
            return getId() == person.getId() && getName().equals(person.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getName());
        }

		@Override
		public String toString() {
			return "Person [id=" + id + ", name=" + name + "]";
		}
    }

    private static Person[] RAW_DATA = new Person[]{
            new Person(0, "Harry"),
            new Person(0, "Harry"), // дубликат
            new Person(1, "Harry"), // тёзка
            new Person(2, "Harry"),
            new Person(3, "Emily"),
            new Person(4, "Jack"),
            new Person(4, "Jack"),
            new Person(5, "Amelia"),
            new Person(5, "Amelia"),
            new Person(6, "Amelia"),
            new Person(7, "Amelia"),
            new Person(8, "Amelia"),
    };
        /*  Raw data:

        0 - Harry
        0 - Harry
        1 - Harry
        2 - Harry
        3 - Emily
        4 - Jack
        4 - Jack
        5 - Amelia
        5 - Amelia
        6 - Amelia
        7 - Amelia
        8 - Amelia

        **************************************************

        Duplicate filtered, grouped by name, sorted by name and id:

        Amelia:
        1 - Amelia (5)
        2 - Amelia (6)
        3 - Amelia (7)
        4 - Amelia (8)
        Emily:
        1 - Emily (3)
        Harry:
        1 - Harry (0)
        2 - Harry (1)
        3 - Harry (2)
        Jack:
        1 - Jack (4)
     */

    public static void personDeduplicatingSortingAndGrouping(Person[] persons) {
    	
    	Arrays.stream(RAW_DATA)
    		.distinct() // удаляю дубликаты. Данный метод для сравнения элементов потока использует equals()
    		.sorted(Comparator.comparingInt(Person::getId))
	    	.collect(Collectors.groupingBy(Person::getName, Collectors.toList()))
	    	.entrySet()
	    	.stream()
	    	.forEach(p -> {
	    		System.out.println("Key:" + p.getKey());
	    		System.out.println("Key:" + p.getValue().size());
	    	});
    }
    
    /**
     * Возвращает первую найденную пару чисел из входного массива, которые в сумме дают заданное значение.
     * Сложность по операциям в наихудшем случае - O((n^2)/2) или просто O(n^2), если вынести и отбросить константу 1/2.
     * Сложность по памяти - O(1)
     * @param input Входной массив
     * @param target Значение суммы двух произвольных искомых чисел, которые такую сумму дают
     * @return Первая пара чисел, чья сумма равна заданной
     * @throws Exception Если массив не содержит ни одной пары чисел, которые в сумме давали бы заданное значение
     */
    public static int[] findCouple(int[] input, int target) throws Exception {
    	
    	for (int y = 0; y < input.length; y++) {
    		// дабы исключить повторяющиеся сочетания для проверки, на каждой новой итерации внешнего цикла
    		// внутренний цикл начинается с инкрементации индекса. 
    		for (int x = y; x < input.length; x++) {
    			if (input[x] <= target && input[y] <= target && input[y] + input[x] == target) {
    				return new int[] {input[y], input[x]};
    			}
    		}
    	}
    	throw new Exception("Входной массив не содержит ни одной пары чисел, которые в сумме давали бы значение " + target + "!");
    }
    
    /**
     * Возвращает true, если набор символов строки input и набор символов строки target пересекаются в наборе символов
     * строки input с сохранением порядка следования символов в наборе символов строки input
     * @param input входная строка
     * @param target целевая строка
     * @return true, если target и input пересекаются в input в порядке следования символов в input
     */
    public static boolean fuzzySearch(String input, String target) {
		
    	int matchRemains = input.length(); // счетчик оставшихся совпадений, равный длине входной строки
    	int inputCharsOffset = 0; // смещение индекса текущего символа во входной строке
    	char[] inputChars = input.toCharArray();
    	char[] targetChars = target.toCharArray();
    	for (var tc : targetChars) { // итерируюсь по порядку по всем символам целевой строки
    		// если текущий символ целевой строки совпадает с символом входной строки с текущим смещением
    		if (tc == inputChars[inputCharsOffset]) {
    			matchRemains--; // то декрементирую счетчик оставшихся совпадений
    			inputCharsOffset++; // и инкрементирую смещение к следующему символу входной строки, отбрасывая тем самым
    			// уже совпавшие символы
    		}
    		if (matchRemains == 0) { // если не осталось никакого символа во входной строке, который еще не совпал с каким-либо
    			// из целевой строки, то выхожу из цикла
    			System.out.println("true");
    			return true;
    		}
    	}
    	System.out.println("false");
    	return false;
    }
    
    public static void main(String[] args) {
        System.out.println("Raw data:");
        System.out.println();

        for (Person person : RAW_DATA) {
            System.out.println(person.id + " - " + person.name);
        }

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();
        System.out.println("Duplicate filtered, grouped by name, sorted by name and id:");
        System.out.println();

        /*
        Task1
            Убрать дубликаты, отсортировать по идентификатору, сгруппировать по имени

            Что должно получиться Key: Amelia
                Value:4
                Key: Emily
                Value:1
                Key: Harry
                Value:3
                Key: Jack
                Value:1
         */

        personDeduplicatingSortingAndGrouping(RAW_DATA);
        System.out.println();

        /*
        Task2

            [3, 4, 2, 7], 10 -> [3, 7] - вывести пару менно в скобках, которые дают сумму - 10
         */

        int[] input = new int[] {3, 4, 2, 7};
        int target = 10;
        try {
        	System.out.println("Исходный массив: " + Arrays.toString(input));
        	System.out.println("Первая пара чисел, дающая в сумме значение "
        			+ target + ": " + Arrays.toString(findCouple(input, target)));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

        /*
        Task3
            Реализовать функцию нечеткого поиска
            
                    fuzzySearch("car", "ca6$$#_rtwheel"); // true
                    fuzzySearch("cwhl", "cartwheel"); // true
                    fuzzySearch("cwhee", "cartwheel"); // true
                    fuzzySearch("cartwheel", "cartwheel"); // true
                    fuzzySearch("cwheeel", "cartwheel"); // false
                    fuzzySearch("lw", "cartwheel"); // false
         */

        System.out.println();
        System.out.println("Тестирование функции нечеткого поиска: \n");
        fuzzySearch("car", "ca6$$#_rtwheel");
        fuzzySearch("cwhl", "cartwheel");
        fuzzySearch("cwhee", "cartwheel");
        fuzzySearch("cartwheel", "cartwheel");
        fuzzySearch("cwheeel", "cartwheel");
        fuzzySearch("lw", "cartwheel");
    }
}
