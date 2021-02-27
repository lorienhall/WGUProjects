class ChainingHashTable:
    def __init__(self, initial_capacity=10):
        #initialize the hash table with empty bucket list entries.
        self.table = []
        for i in range(initial_capacity):
            self.table.append([])

    # Inserts a new item into the hash table.
    def insert(self, p_id, package):  # does both insert and update
        # get the bucket list where this item will go.
        bucket = hash(p_id) % len(self.table)
        bucket_list = self.table[bucket]

        # update key if it is already in the bucket
        for kv in bucket_list:
            if kv[0] == p_id:
                kv[1] = package
                return True

        # if not, insert the item to the end of the bucket list.
        key_value = [p_id, package]
        bucket_list.append(key_value)
        return True

    # Searches for an item with matching key in the hash table.
    def search(self, p_id):
        # get the bucket list where this key would be.
        bucket = p_id % 10
        bucket_list = self.table[bucket]

        # search for the key in the bucket list
        for kv in bucket_list:
            # print (key_value)
            if kv[0] == p_id:
                return kv[1]  # value
        return None

    # Removes an item with matching key from the hash table.
    def remove(self, p_id):
        # get the bucket list where this item will be removed from.
        bucket = p_id % 10
        bucket_list = self.table[bucket]

        # remove the item from the bucket list if it is present.
        for kv in bucket_list:
            if kv[0] == p_id:
                bucket_list.remove([kv[0], kv[1]])


# My chaining hash table
myHash = ChainingHashTable()
