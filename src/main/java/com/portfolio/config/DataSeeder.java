package com.portfolio.config;

import com.portfolio.domain.Asset;
import com.portfolio.domain.AssetType;
import com.portfolio.domain.User;
import com.portfolio.repository.AssetRepository;
import com.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(User.builder().name("John Doe").email("john@example.com").build());
            userRepository.save(User.builder().name("Jane Smith").email("jane@example.com").build());
        }

        if (assetRepository.count() == 0) {
            assetRepository.saveAll(List.of(
                    Asset.builder().symbol("AAPL").type(AssetType.STOCK).build(),
                    Asset.builder().symbol("GOOGL").type(AssetType.STOCK).build(),
                    Asset.builder().symbol("BTC").type(AssetType.CRYPTO).build(),
                    Asset.builder().symbol("ETH").type(AssetType.CRYPTO).build()
            ));
        }
    }
}
