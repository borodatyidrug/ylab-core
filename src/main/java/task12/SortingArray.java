package task12;

public class SortingArray {

	public static int partition(int[] array, int left, int right) {
		int pivot = (array[left] + array[right]) / 2;
		int l = left;
		int r = right;
		while (true) {
			while (array[l] < pivot) {
				l++;
			}
			while (array[r] > pivot) {
				r--;
			}
			if (l >= r) {
				return r;
			} else {
				int swapBuffer = array[l];
				array[l] = array[r];
				array[r] = swapBuffer;
				l++;
				r--;
			}
		}
	}
	
	public static void quickHoareSort(int[] array, int left, int right) {
		if (left < right) {
			int border = partition(array, left, right);
			quickHoareSort(array, left, border);
			quickHoareSort(array, border + 1, right);
		}
	}
	
	public static String arrayToString(int[] array) {
		StringBuilder builder = new StringBuilder();
		for (var x : array) {
			builder.append(x);
			builder.append("\t");
		}
		return builder.toString();
	}
	
	/**
	 * Проверяет перебором, отсортированны ли элементы переданного массива.
	 * Массив считается отсортированным в порядке возрастания, если каждый
	 * следующий элемент, начиная с первого, не меньше предыдущего.
	 * @param array входной массив
	 * @return Отсортирован или нет
	 */
	public static boolean isSorted(int[] array) {
		for (int i = 1; i < array.length; i++) {
			if (array[i] < array[i - 1]) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		int[] target, sorted, nearlySorted, manyDuplicates, signed;
		
		target = new int[] {5, 6, 3, 2, 5, 1, 4, 9}; // Целевой массив по условию задачи
		sorted = new int[] {1, 2, 3, 4, 5, 5, 6, 9}; // Отсортированный массив
		nearlySorted = new int[] {1, 3, 2, 4, 5, 6, 5, 9}; // Близкий к отсортированному
		manyDuplicates = new int[] {5, 6, 5, 2, 5, 1, 4, 5}; // Много дубликатов
		signed = new int[] {-5, 6, -3, 2, -5, 1, -4, 9}; // Целые со знаком и дубликатами
		
		quickHoareSort(target, 0, target.length - 1);
		quickHoareSort(sorted, 0, sorted.length - 1);
		quickHoareSort(nearlySorted, 0, nearlySorted.length - 1);
		quickHoareSort(manyDuplicates, 0, manyDuplicates.length - 1);
		quickHoareSort(signed, 0, signed.length - 1);
		
		System.out.println(arrayToString(target));
		System.out.println(arrayToString(sorted));
		System.out.println(arrayToString(nearlySorted));
		System.out.println(arrayToString(manyDuplicates));
		System.out.println(arrayToString(signed));
		
		// Т.к. target, sorted и nearlySorted - перестановки одного и того же массива, то
		// достаточно проверить, что они отсортированны
		assert isSorted(target) : "Массив не отсортирован!";
		assert isSorted(sorted) : "Массив не отсортирован!";
		assert isSorted(nearlySorted) : "Массив не отсортирован!";
		assert isSorted(manyDuplicates) : "Массив не отсортирован!";
		assert isSorted(signed) : "Массив не отсортирован!";
	}

}
