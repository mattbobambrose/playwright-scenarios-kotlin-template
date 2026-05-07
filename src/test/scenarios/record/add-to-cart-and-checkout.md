# Add to Cart and Checkout

**URL:** /

A shopper arrives on the homepage, browses to the catalog, opens *A Quiet Revolution*, adds it to the cart, updates the quantity from 1 to 5, and proceeds to checkout.

> Recorded by `/record-scenario` from `http://localhost:8080`. Review before feeding into `/scenario-to-tests`.

## Test 1: Add 'A Quiet Revolution' to cart, update quantity, and check out

- **Expected:** The hero heading shows "Your next favorite book is on the shelf.".
- **Expected:** The featured book card with test-id `featured-8` shows the title "The Lunar Protocol".
- **Expected:** A "Browse the catalog" link is visible in the hero section.
- **Expected:** A "Log in" link is visible in the hero section.
- **Action:** Click the "Catalog" navigation link.
- **Action:** Click the "📗 A Quiet Revolution" book link.
- **Action:** Click "Add to Cart".
- **Expected:** The URL changes to `/cart`.
- **Expected:** The cart quantity field for item 5 contains "1".
- **Action:** Change the cart quantity field for item 5 to "5".
- **Action:** Click the "Update" button for item 5.
- **Expected:** The cart quantity field for item 5 contains "5".
- **Action:** Click the "Check out" link.
- **Expected:** The URL changes to `/checkout`.
- **Expected:** The "Checkout" heading (h1) is visible.
