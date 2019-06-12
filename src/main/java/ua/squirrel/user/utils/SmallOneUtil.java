package ua.squirrel.user.utils;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class SmallOneUtil {
	
	public LocalDate convertDate(String date) {
		String[] splite = date.split("[.]");
		LocalDate calendar = LocalDate.of(Integer.parseInt(splite[2].trim()), Integer.parseInt(splite[1].trim()),
				Integer.parseInt(splite[0].trim()));

		return calendar;
	}
	
	// формирует мапу с id в качестве ключа и строй с двумя значениями разделеными символами val
	public Map <Long, String> spliteIdValue1Value2(String data, String regex) {
		Map <Long, String> idsValues = new HashMap<>();
		String [] result = data.split(regex); 
		for (int i = 0; i < result.length; i+=3) {
			idsValues.put(Long.parseLong(result[i]), result[i+1]+"val"+result[i+2]);
		}
		return idsValues;
	}
	
	//метод формирует строку с id значением и временем обновления
	public String concatIdsValueDateToString(Long id, Integer value, String regex) {
		StringBuilder str = new StringBuilder();
			str.append(id + ":" + value + regex + new Date().getTime() + "date");
		return str.toString();
	}
	
	
	// метод формирует строку с данными Ид и значение
		public String concatIdsValueToString(Map<Long, Integer> map, String regex) {
			StringBuilder str = new StringBuilder();
			map.keySet().forEach(id -> {
				str.append(id + ":" + map.get(id) + regex);
			});
			return str.toString();
		}
	
	

	// метод воздащает Ид и значение
	public Map<Long, Integer> spliteIdsValue(String data, String regex) {
		Map<Long, Integer> result = new HashMap<>();
		if (data != null && data.length() > 0) {
			String[] daraParse = data.split(regex);
			for (int i = 0; i < daraParse.length; i++) {
				String[] parse = daraParse[i].split(":");
				result.put(Long.parseLong(parse[0]), Integer.parseInt(parse[1]));
			}
		}
		return result;
	}

	// метод возращает список ид
	public Set<Long> spliteIds(String data, String regex) {
		return spliteIdsValue( data,  regex).keySet();
	}
	

	// метод удаляет все повторения одной Map в другой
	public Map<Long, Integer> removeDublicateMap(Map<Long, Integer> origin, Map<Long, Integer> dublicateRemove) {
		origin.keySet().forEach(id -> {
			dublicateRemove.remove(id);
		});
		return dublicateRemove;
	}
}
