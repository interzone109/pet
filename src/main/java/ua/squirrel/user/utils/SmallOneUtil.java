package ua.squirrel.user.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class SmallOneUtil {
	
	//метод воздащает Ид и значение
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
	
	//метод возращает список ид
	public Set<Long> spliteIds(String data, String regex) {
		Set<Long> result = new HashSet<>();
		if (data != null && data.length() > 0) {
			String[] daraParse = data.split(regex);
			for (int i = 0; i < daraParse.length; i++) {
				result.add(Long.parseLong(daraParse[0]));
			}
		}
		return result;
	}
	
}
