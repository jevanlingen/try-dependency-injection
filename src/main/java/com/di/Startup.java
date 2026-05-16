import com.di.architecture.SetupConfigurer;

void main() throws InterruptedException {
    SetupConfigurer.configure();

    // Stay alive
    while (true) {
        Thread.sleep(1000);
    }
}
