package tech.erben.java17.streams.movies;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        Device device1 = new Device("1");
        Device device2 = new Device("2");
        Device device3 = new Device("3");


        var firstUser = new User(List.of(device1, device2));
        var husbandOfFirstUser = new User(List.of(device1));
        var secondUser = new User(List.of(device3));

        Stream.of(firstUser, husbandOfFirstUser, secondUser)
            .flatMap(user -> user.devices.stream())
            .distinct()
            .sorted(Comparator.comparing((Device o) -> o.id).reversed())
            .forEach(System.out::println);

    }

    public static class User {
        private final List<Device> devices;

        public User(List<Device> devices) {
            this.devices = devices;
        }

        public List<Device> getDevices() {
            return devices;
        }
    }

    public static class Device {
        private final String id;

        public Device(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Device{" +
                "id='" + id + '\'' +
                '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Device device = (Device) o;
            return Objects.equals(id, device.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}
