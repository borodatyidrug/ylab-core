package task11;

public class MinMaxMean {
	
	/**
	 * Проверяет перебором, отсортированны ли элементы переданного массива.
	 * Массив считается отсортированным в порядке возрастания, если каждый
	 * следующий элемент, начиная с первого, не меньше предыдущего.
	 * @param array входной массив
	 * @return Отсортирован или нет
	 */
	public static boolean isSorted(long[] array) {
		for (int i = 1; i < array.length; i++) {
			if (array[i] < array[i - 1]) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		/**
		 * Класс реализует линейный конгруэнтный метод генерации псевдослучайных чисел от
		 * начального r0. В боевых условиях r0 можно сделать функцией системного времени в наносекундах.
		 * Для отладки удобно использовать в неизменном виде как константу.
		 * @author borodatyidrug
		 */
		class LCMRandom {
			
			private long m, k, b, r0, nextRandom;
			
			public LCMRandom(long m, long k, long b, long r0) {
				this.m = m;
				this.k = k;
				this.b = b;
				this.r0 = r0;
			}
			
			public long nextLong() {
				nextRandom = Math.abs((k * r0 + b) % m);
				r0 = nextRandom;
				return nextRandom >> 16; // Отбрасываю младшие 16 бит, чтобы числа получились более удобными и читабельными :)
			}
		}
		
		class Matrix {
			
			private long height, width, min, max;
			private long[][] matrix;
			private long[] flatten;
			private double mean;
			
			/**
			 * Матрица со всеми требуемыми по заданию статистическими характеристиками конструируется сразу в конструкторе
			 * @param height Кол-во строк
			 * @param width Кол-во столбцов
			 * @param rnd Генератор псевдослучайных чисел
			 */
			public Matrix(int height, int width, LCMRandom rnd) {
				this.height = height;
				this.width = width;
				matrix = new long[height][width];
				fillByRandoms(rnd);
				getFlatten();
				quickHoareSort(flatten, 0, flatten.length - 1);
				min = flatten[0];
				max = flatten[flatten.length - 1];
				mean = mean(flatten);
			}
			
			private void fillByRandoms(LCMRandom rnd) {
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						matrix[i][j] = rnd.nextLong();
					}
				}
			}
			
			public long[][] getMatrix() {
				return matrix;
			}
			
			@Override
			public String toString() {
				StringBuilder builder = new StringBuilder();
				for (var y : matrix) {
					for (var x : y) {
						builder.append(x);
						builder.append("\t");
					}
					builder.append("\n");
				}
				return builder.toString();
			}
			
			/**
			 * Выполняет разбиение Хоара для текущего подмассива
			 * @param array
			 * @param left
			 * @param right
			 * @return Индекс новой границы
			 */
			private int partition(long[] array, int left, int right) {
				long pivot = (array[left] + array[right]) / 2;
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
						long swapBuffer = array[l];
						array[l] = array[r];
						array[r] = swapBuffer;
						l++;
						r--;
					}
				}
			}
			
			private void quickHoareSort(long[] array, int left, int right) {
				if (left < right) {
					int border = partition(array, left, right);
					quickHoareSort(array, left, border);
					quickHoareSort(array, border + 1, right);
				}
			}
			
			public String arrayToString(long[] array) {
				StringBuilder builder = new StringBuilder();
				for (var x : array) {
					builder.append(x);
					builder.append("\n");
				}
				return builder.toString();
			}
			
			public long[] getFlatten(long[][] array) {
				int height = array.length;
				int width = array[0].length;
				long[] flatArray = new long[height * width];
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						flatArray[y * width + x] = array[y][x]; 
					}
				}
				return flatArray;
			}
			
			public long[] getFlatten() {
				if (flatten != null) {
					return flatten;
				} else {
					flatten = getFlatten(getMatrix());
					return flatten;
				}
			}
			
			public long min() {
				return min;
			}
			
			public long max() {
				return max;
			}
			
			public double mean(long[] array) {
				long sum = 0;
				for(var x : array) {
					sum += x;
				}
				return ((double) sum) / array.length;
			}
			
			public double mean() {
				return mean;
			}
		}
		
		LCMRandom rnd = new LCMRandom((int) Math.pow(2, 31) - 1, 1220703125, 7, 7);
	
		Matrix matrix = new Matrix(10, 10, rnd);
		System.out.println("Исходный двумерный массив:\n");
		System.out.println(matrix);
		System.out.println("Уплощенный отсортированный массив:\n");
		System.out.println(matrix.arrayToString(matrix.getFlatten()));
		System.out.println("Минимальный элемент массива: " + matrix.min());
		System.out.println("Максимальный элемента массива: " + matrix.max());
		System.out.println("Среднее значение: " + matrix.mean());
		
		assert isSorted(matrix.getFlatten()) : "Промежуточный (уплощенный) массив не отсортирован "
				+ "в порядке возрастания. Это значит, что минимум и максимум - не верны.";
	}

}
