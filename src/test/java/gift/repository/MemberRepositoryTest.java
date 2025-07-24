package gift.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import gift.domain.Member;
import gift.domain.embed.Email;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        jdbcClient.sql("ALTER TABLE member ALTER COLUMN id RESTART WITH 1").update();
    }

    @Test
    void saveTest() {
        // given
        Member member = new Member("test@test.com", "dbPassword");

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getId()).isEqualTo(1L);
        assertThat(savedMember.getEmail()).isEqualTo("test@test.com");
        assertThat(savedMember.getPassword()).isEqualTo("dbPassword");
    }

    @Test
    void findByEmailTest() {
        // given
        memberRepository.save(new Member("test@test.com", "dbPassword"));

        // when
        Optional<Member> member = memberRepository.findByEmail(new Email("test@test.com"));

        // then
        assertThat(member.get().getId()).isEqualTo(1L);
        assertThat(member.get().getEmail()).isEqualTo("test@test.com");
        assertThat(member.get().getPassword()).isEqualTo("dbPassword");
    }

    @Test
    void findByEmailFailTest() {
        // given
        memberRepository.save(new Member("test@test.com", "dbPassword"));

        // when
        Optional<Member> member = memberRepository.findByEmail(new Email("test2@test.com"));

        // then
        assertThat(member).isEmpty();
    }
}
