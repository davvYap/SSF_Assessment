package ibf2022.batch2.ssf.frontcontroller.respositories;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthenticationRepository {

	// TODO Task 5
	// Use this class to implement CRUD operations on Redis
	@Autowired
	@Qualifier("authentication")
	private RedisTemplate<String, String> redisTemplate;

	public void disableUser(String username) {
		redisTemplate.opsForValue().set(username, username);
		redisTemplate.opsForValue().getAndExpire(username, 1800, TimeUnit.SECONDS);
	}

	public boolean isLocked(String username) {
		String name = redisTemplate.opsForValue().get(username);
		if (name != null) {
			return true;
		}
		return false;
	}
}
