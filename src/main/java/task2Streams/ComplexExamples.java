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
            new Person(8, "Amelia")
    };
    
    private static Person[] RAW_DATA_WITH_NULL = new Person[]{
    		
            new Person(0, "Harry"),
            new Person(0, "Harry"), // дубликат
            new Person(1, "Harry"), // тёзка
            new Person(2, "Harry"),
            new Person(3, "Emily"),
            new Person(4, "Jack"),
            new Person(4, "Jack"),
            new Person(5, "Amelia"),
            null,
            new Person(5, "Amelia"),
            new Person(6, "Amelia"),
            new Person(7, "Amelia"),
            new Person(8, "Amelia")
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

    public static Map<String, List<Person>> personDeduplicatingSortingAndGrouping(Person[] persons) {
    	
    	return Arrays.stream(persons)
    			.map(p -> Optional.ofNullable(p).orElse(new Person(0, "Default")))
    			.distinct() // удаляю дубликаты. Данный метод для сравнения элементов потока использует equals()
    			.sorted(Comparator.comparingInt(Person::getId))
    			.collect(Collectors.groupingBy(Person::getName, Collectors.toList()));
    }
    
    public static String personsMapToString(Map<String, List<Person>> personsMap) {
    	
    	StringBuilder builder = new StringBuilder();
    	personsMap.entrySet()
		    	.stream()
		    	.forEach(p -> {
		    		builder.append("Key: ");
		    		builder.append(p.getKey());
		    		builder.append("\n");
		    		builder.append("Value: ");
		    		builder.append(p.getValue().size());
		    		builder.append("\n");
		    	});
    	
    	return builder.toString().trim();
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
    	
    	int[] inputArray = Optional.ofNullable(input)
    			.orElseThrow(() -> new IllegalArgumentException("Массив не должен быть null!"));
    	
    	for (int y = 0; y < inputArray.length; y++) {
    		// дабы исключить повторяющиеся сочетания для проверки, на каждой новой итерации внешнего цикла
    		// внутренний цикл начинается с инкрементации индекса. 
    		for (int x = y; x < inputArray.length; x++) {
    			if (inputArray[x] <= target && inputArray[y] <= target && inputArray[y] + inputArray[x] == target) {
    				return new int[] {inputArray[y], inputArray[x]};
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
		
    	int matchRemains = Optional.ofNullable(input).orElseThrow().length(); // счетчик оставшихся совпадений, равный длине входной строки
    	int inputCharsOffset = 0; // смещение индекса текущего символа во входной строке
    	char[] inputChars = input.toCharArray();
    	char[] targetChars = Optional.ofNullable(target).orElseThrow().toCharArray();
    	
    	for (var tc : targetChars) { // итерируюсь по порядку по всем символам целевой строки
    		// если текущий символ целевой строки совпадает с символом входной строки с текущим смещением
    		if (tc == inputChars[inputCharsOffset]) {
    			matchRemains--; // то декрементирую счетчик оставшихся совпадений
    			inputCharsOffset++; // и инкрементирую смещение к следующему символу входной строки, отбрасывая тем самым
    			// уже совпавшие символы
    		}
    		if (matchRemains == 0) { // если не осталось никакого символа во входной строке, который еще не совпал с каким-либо
    			// из целевой строки, то выхожу из цикла
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public static void main(String[] args) {
    	
        System.out.println("Raw data:");
        System.out.println();

        for (Person person : RAW_DATA) {
            System.out.println(person.id + " - " + person.name);
        }

        System.out.println();

        /*
        Task1
            Убрать дубликаты, отсортировать по идентификатору, сгруппировать по имени

            Что должно получиться 
            	Key: Amelia
                Value:4
                Key: Emily
                Value:1
                Key: Harry
                Value:3
                Key: Jack
                Value:1
         */

        Map<String, List<Person>> personsMap = 
        		personDeduplicatingSortingAndGrouping(RAW_DATA);
        Map<String, List<Person>> personsMapWithDefault = 
        		personDeduplicatingSortingAndGrouping(RAW_DATA_WITH_NULL);
        
        String expected = 
        		"Key: Amelia\n"
        		+ "Value: 4\n"
        		+ "Key: Emily\n"
        		+ "Value: 1\n"
        		+ "Key: Harry\n"
        		+ "Value: 3\n"
        		+ "Key: Jack\n"
        		+ "Value: 1";
        
        String expectedIfContainsNullPerson = 
        		"Key: Amelia\n"
        		+ "Value: 4\n"
        		+ "Key: Emily\n"
        		+ "Value: 1\n"
        		+ "Key: Harry\n"
        		+ "Value: 3\n"
        		+ "Key: Jack\n"
        		+ "Value: 1\n"
        		+ "Key: Default\n"
        		+ "Value: 1";
        
        String actual = personsMapToString(personsMap);
        assert expected.equals(actual);
        
        actual = personsMapToString(personsMapWithDefault);
        assert expectedIfContainsNullPerson.equals(actual);

        /*
        Task2

            [3, 4, 2, 7], 10 -> [3, 7] - вывести пару менно в скобках, которые дают сумму - 10
         */

        int[] input, expectedArray, actualArray;
        int target = 10;
        
        input = new int[] {3, 4, 2, 7};
        expectedArray = new int[] {3, 7};
        
        try {
        	
			actualArray = findCouple(input, target);
			assert Arrays.equals(expectedArray, actualArray);
			
			findCouple(null, target); // Проверяю вброс исключения, если входной массив - null
			
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

        assert fuzzySearch("car", "ca6$$#_rtwheel");
        assert fuzzySearch("cwhl", "cartwheel");
        assert fuzzySearch("cwhee", "cartwheel");
        assert fuzzySearch("cartwheel", "cartwheel");
        assert !fuzzySearch("cwheeel", "cartwheel");
        assert !fuzzySearch("lw", "cartwheel");
        
        try {
        	fuzzySearch(null, null); // Проверяю вброс исключения, если входные строки - null
        } catch (NoSuchElementException e) {
        	System.out.println("Входные строки не должны быть null!");
        }
    }
}
