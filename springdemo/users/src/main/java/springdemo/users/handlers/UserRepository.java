package springdemo.users.handlers;

import org.springframework.data.jpa.repository.JpaRepository;
import springdemo.users.models.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
}
