package com.udacity.vehicles.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {

    private final CarRepository repository;

    private final MapsClient mapsClient;

    private final PriceClient priceClient;

    public CarService(CarRepository repository, MapsClient mapsClient, PriceClient priceClient) {
        /**
         * TODO: Add the Maps and Pricing Web Clients you create
         *   in VehiclesApiApplication as arguments and set them here.
         */
        this.repository = repository;
        this.mapsClient = mapsClient;
        this.priceClient = priceClient;    
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        if (!repository.findById(id).isPresent()) {
            throw new CarNotFoundException();
        }
        Car car = repository.findById(id).get();

        /**
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */
        String price = priceClient.getPrice(id);
        car.setPrice(price);

        /**
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */

        Location location = mapsClient.getAddress(car.getLocation());
        car.setLocation(location);

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        try {
            if (car.getId() != null) {
                return repository.findById(car.getId())
                        .map(carToBeUpdated -> {
                            carToBeUpdated.setDetails(car.getDetails());
                            carToBeUpdated.setLocation(car.getLocation());
                            return repository.save(carToBeUpdated);
                        }).orElseThrow(CarNotFoundException::new);
            }
    
            return repository.save(car);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
         if (!repository.findById(id).isPresent()) {
            throw new CarNotFoundException();
        }
        Car car = repository.findById(id).get();

        repository.delete(car);
    }
}