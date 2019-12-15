package com.tomi5548319.getpass;

import java.lang.reflect.AccessibleObject;
import java.util.Arrays;
import java.util.Random;

class Password {
    private static String mSeed;
    private static char[] ACTUAL_ALPHABET = new char[1000];
    private static int mLength;

    static String generate(String name, String seed, int length, boolean small, boolean big, boolean numbers, boolean basicChars, boolean advancedChars, String customChars, String key){
        char[] c_Name = getName(name);
        char[] c_Key = getKey(key);
        char[] c_Seed = seed.toCharArray();

        if(length > 16)
            length = 16;

        c_Name = AES_Encrypt(c_Name, c_Key);
        c_Name = AES_Encrypt(c_Name, c_Seed);

        changeAlphabet(small, big, numbers, basicChars, advancedChars, customChars);

        for(int i=0; i<length; i++)
            c_Name[i] = ACTUAL_ALPHABET[c_Name[i] % mLength];

        name = new String(c_Name, 0, length);
        mSeed = new String(c_Seed);

        return name;
    }

    static String generate(String name, int length, boolean small, boolean big, boolean numbers, boolean basicChars, boolean advancedChars, String customChars, String key){
        char[] c_Name = getName(name);
        char[] c_Key = getKey(key);
        char[] c_Seed = generateSeed();

        if(length > 16)
            length = 16; // TODO Generate longer passwords than 16 too

        c_Name = AES_Encrypt(c_Name, c_Key);
        c_Name = AES_Encrypt(c_Name, c_Seed);

        changeAlphabet(small, big, numbers, basicChars, advancedChars, customChars);

        for(int i=0; i<length; i++)
            c_Name[i] = ACTUAL_ALPHABET[c_Name[i] % mLength];

        name = new String(c_Name, 0, length);
        mSeed = new String(c_Seed);


        return name;
    }

    private static void changeAlphabet(boolean small, boolean big, boolean numbers, boolean basicChars, boolean advancedChars, String customChars){
        // Append known alphabets to ACTUAL_ALPHABET

        mLength = 0;

        if(small){
            System.arraycopy(SMALL_ALPHABET, 0, ACTUAL_ALPHABET, mLength, SMALL_ALPHABET.length);
            mLength += SMALL_ALPHABET.length;
        }

        if(big){
            System.arraycopy(BIG_ALPHABET, 0, ACTUAL_ALPHABET, mLength, BIG_ALPHABET.length);
            mLength += BIG_ALPHABET.length;
        }

        if(numbers){
            System.arraycopy(NUMBERS, 0, ACTUAL_ALPHABET, mLength, NUMBERS.length);
            mLength += NUMBERS.length;
        }

        if(basicChars){
            System.arraycopy(BASIC_CHARS, 0, ACTUAL_ALPHABET, mLength, BASIC_CHARS.length);
            mLength += BASIC_CHARS.length;
        }

        if(advancedChars){
            System.arraycopy(ADVANCED_CHARS, 0, ACTUAL_ALPHABET, mLength, ADVANCED_CHARS.length);
            mLength += ADVANCED_CHARS.length;
        }

        if(!customChars.equals("")){
            char[] custom = customChars.toCharArray();
            System.arraycopy(custom, 0, ACTUAL_ALPHABET, mLength, custom.length);
            mLength += custom.length;
        }
    }

    static String getSeed(){
        return mSeed;
    }

    private static char[] generateSeed(){

        // TODO Random
        // TODO SecureRandom

        char[] seed = new char[16];

        Random rand = new Random();

        for(int i=0; i<16; i++)
            seed[i] = (char) rand.nextInt(256);

        return seed;
    }

    private static char[] getName(String name){ // Modify name length
        char[] Name = name.toCharArray(); // "Name" => [{'N'},{'a'},{'m'},{'e'}]

        if(Name.length <= 16){ // name is too short or ideal
            int i = 0;
            while(Name.length < 16){
                Name = Arrays.copyOf(Name, Name.length+1); // [{'N'},{'a'},{'m'},{'e'}] => [{'N'},{'a'},{'m'},{'e'},{''}]
                Name[Name.length-1] = Name[i]; // [{'N'},{'a'},{'m'},{'e'},{'N'}]
                i++;
            }
        }
        else // name is too long
            Name = Arrays.copyOf(Name, 16);

        return Name;
    }

    private static char[] getKey(String key){ // Modify key length
        char[] Key = key.toCharArray(); // "Key" => [{'K'},{'e'},{'y'}]

        if(Key.length <= 16){ // key is too short or ideal
            int i = 0;
            while(Key.length < 16){
                Key = Arrays.copyOf(Key, Key.length+1); // [{'K'},{'e'},{'y'}] => [{'K'},{'e'},{'y'},{''}]
                Key[Key.length-1] = Key[i]; // [{'K'},{'e'},{'y'},{'K'}]
                i++;
            }
        }
        else // key is too long
            Key = Arrays.copyOf(Key, 16);

        return Key;
    }

    private static char[] AES_Encrypt(char[] state, char[] key){

        // TODO Change number of ronds depending on the input
        final int numberOfRounds = 9;

        // Expand the keys
        char[] expandedKey = KeyExpansion(key);

        state = AddRoundKey(state, key);

        // Rounds
        for(int i=0; i<numberOfRounds; i++){
            state = SubBytes(state);
            state = ShiftRows(state);
            state = MixColumns(state);
            state = AddRoundKey(state, new char[]{  expandedKey[16*(i+1)], expandedKey[16*(i+1)+1], expandedKey[16*(i+1)+2], expandedKey[16*(i+1)+3],
                                            expandedKey[16*(i+1)+4], expandedKey[16*(i+1)+5], expandedKey[16*(i+1)+6], expandedKey[16*(i+1)+7],
                                            expandedKey[16*(i+1)+8], expandedKey[16*(i+1)+9], expandedKey[16*(i+1)+10], expandedKey[16*(i+1)+11],
                                            expandedKey[16*(i+1)+12], expandedKey[16*(i+1)+13], expandedKey[16*(i+1)+14], expandedKey[16*(i+1)+15]});
        }

        // Final Round
        state = SubBytes(state);
        state = ShiftRows(state);
        state = AddRoundKey(state, new char[]{  expandedKey[160], expandedKey[161], expandedKey[162], expandedKey[163],
                                        expandedKey[164], expandedKey[165], expandedKey[166], expandedKey[167],
                                        expandedKey[168], expandedKey[169], expandedKey[170], expandedKey[171],
                                        expandedKey[172], expandedKey[173], expandedKey[174], expandedKey[175]});

        return state;
    }

    private static char[] KeyExpansion(char[] inputKey){

        char[] expandedKeys = new char[176];

        // The first 16 bytes are the original key
        System.arraycopy(inputKey, 0, expandedKeys, 0, 16);

        int bytesGenerated = 16;
        char rconIteration = 1;
        char[] temp = new char[4];

        while(bytesGenerated < 176){

            // Read 4 bytes for the core
            System.arraycopy(expandedKeys, bytesGenerated - 4, temp, 0, 4);

            // Perform the core once for each 16 byte key
            if(bytesGenerated % 16 == 0){
                temp = KeyExpansionCore(temp, rconIteration);
                rconIteration++;
            }

            bytesGenerated += 4;
        }

        return expandedKeys;
    }

    private static char[] SubBytes(char[] state){
        for(int i=0; i<16; i++)
            state[i] = SUB[state[i]];

        return state;
    }

    private static final char[] SUB = {
            //		 00		 01		 02		 03		 04		 05		 06		 07		 08		 09		 0a		 0b		 0c		 0d		 0e		 0f
            /*00*/	0x63,	0x7c,	0x77,	0x7b,	0xf2,	0x6b,	0x6f,	0xc5,	0x30,	0x01,	0x67,	0x2b,	0xfe,	0xd7,	0xab,	0x76,
            /*10*/	0xca,	0x82,	0xc9,	0x7d,	0xfa,	0x59,	0x47,	0xf0,	0xad,	0xd4,	0xa2,	0xaf,	0x9c,	0xa4,	0x72,	0xc0,
            /*20*/	0xb7,	0xfd,	0x93,	0x26,	0x36,	0x3f,	0xf7,	0xcc,	0x34,	0xa5,	0xe5,	0xf1,	0x71,	0xd8,	0x31,	0x15,
            /*30*/	0x04,	0xc7,	0x23,	0xc3,	0x18,	0x96,	0x05,	0x9a,	0x07,	0x12,	0x80,	0xe2,	0xeb,	0x27,	0xb2,	0x75,
            /*40*/	0x09,	0x83,	0x2c,	0x1a,	0x1b,	0x6e,	0x5a,	0xa0,	0x52,	0x3b,	0xd6,	0xb3,	0x29,	0xe3,	0x2f,	0x84,
            /*50*/	0x53,	0xd1,	0x00,	0xed,	0x20,	0xfc,	0xb1,	0x5b,	0x6a,	0xcb,	0xbe,	0x39,	0x4a,	0x4c,	0x58,	0xcf,
            /*60*/	0xd0,	0xef,	0xaa,	0xfb,	0x43,	0x4d,	0x33,	0x85,	0x45,	0xf9,	0x02,	0x7f,	0x50,	0x3c,	0x9f,	0xa8,
            /*70*/	0x51,	0xa3,	0x40,	0x8f,	0x92,	0x9d,	0x38,	0xf5,	0xbc,	0xb6,	0xda,	0x21,	0x10,	0xff,	0xf3,	0xd2,
            /*80*/	0xcd,	0x0c,	0x13,	0xec,	0x5f,	0x97,	0x44,	0x17,	0xc4,	0xa7,	0x7e,	0x3d,	0x64,	0x5d,	0x19,	0x73,
            /*90*/	0x60,	0x81,	0x4f,	0xdc,	0x22,	0x2a,	0x90,	0x88,	0x46,	0xee,	0xb8,	0x14,	0xde,	0x5e,	0x0b,	0xdb,
            /*a0*/	0xe0,	0x32,	0x3a,	0x0a,	0x49,	0x06,	0x24,	0x5c,	0xc2,	0xd3,	0xac,	0x62,	0x91,	0x95,	0xe4,	0x79,
            /*b0*/	0xe7,	0xc8,	0x37,	0x6d,	0x8d,	0xd5,	0x4e,	0xa9,	0x6c,	0x56,	0xf4,	0xea,	0x65,	0x7a,	0xae,	0x08,
            /*c0*/	0xba,	0x78,	0x25,	0x2e,	0x1c,	0xa6,	0xb4,	0xc6,	0xe8,	0xdd,	0x74,	0x1f,	0x4b,	0xbd,	0x8b,	0x8a,
            /*d0*/	0x70,	0x3e,	0xb5,	0x66,	0x48,	0x03,	0xf6,	0x0e,	0x61,	0x35,	0x57,	0xb9,	0x86,	0xc1,	0x1d,	0x9e,
            /*e0*/	0xe1,	0xf8,	0x98,	0x11,	0x69,	0xd9,	0x8e,	0x94,	0x9b,	0x1e,	0x87,	0xe9,	0xce,	0x55,	0x28,	0xdf,
            /*f0*/	0x8c,	0xa1,	0x89,	0x0d,	0xbf,	0xe6,	0x42,	0x68,	0x41,	0x99,	0x2d,	0x0f,	0xb0,	0x54,	0xbb,	0x16
    };

    private static char[] ShiftRows(char[] state){
        char[] tmp = new char[16];

        tmp[0] = state[0];
        tmp[1] = state[5];
        tmp[2] = state[10];
        tmp[3] = state[15];

        tmp[4] = state[4];
        tmp[5] = state[9];
        tmp[6] = state[14];
        tmp[7] = state[3];

        tmp[8] = state[8];
        tmp[9] = state[13];
        tmp[10] = state[2];
        tmp[11] = state[7];

        tmp[12] = state[12];
        tmp[13] = state[1];
        tmp[14] = state[6];
        tmp[15] = state[11];

        System.arraycopy(tmp, 0, state, 0, 16);

        return state;
    }

    private static char[] MixColumns(char[] state){

        char[] tmp = new char[16];

        tmp[0] = (char) (MUL2[state[0]] ^ MUL3[state[1]] ^ state[2] ^ state[3]);
        tmp[1] = (char) (state[0] ^ MUL2[state[1]] ^ MUL3[state[2]] ^ state[3]);
        tmp[2] = (char) (state[0] ^ state[1] ^ MUL2[state[2]] ^ MUL3[state[3]]);
        tmp[3] = (char) (MUL3[state[0]] ^ state[1] ^ state[2] ^ MUL2[state[3]]);

        tmp[4] = (char) (MUL2[state[4]] ^ MUL3[state[5]] ^ state[6] ^ state[7]);
        tmp[5] = (char) (state[4] ^ MUL2[state[5]] ^ MUL3[state[6]] ^ state[7]);
        tmp[6] = (char) (state[4] ^ state[5] ^ MUL2[state[6]] ^ MUL3[state[7]]);
        tmp[7] = (char) (MUL3[state[4]] ^ state[5] ^ state[6] ^ MUL2[state[7]]);

        tmp[8] = (char) (MUL2[state[8]] ^ MUL3[state[9]] ^ state[10] ^ state[11]);
        tmp[9] = (char) (state[8] ^ MUL2[state[9]] ^ MUL3[state[10]] ^ state[11]);
        tmp[10] = (char) (state[8] ^ state[9] ^ MUL2[state[10]] ^ MUL3[state[11]]);
        tmp[11] = (char) (MUL3[state[8]] ^ state[9] ^ state[10] ^ MUL2[state[11]]);

        tmp[12] = (char) (MUL2[state[12]] ^ MUL3[state[13]] ^ state[14] ^ state[15]);
        tmp[13] = (char) (state[12] ^ MUL2[state[13]] ^ MUL3[state[14]] ^ state[15]);
        tmp[14] = (char) (state[12] ^ state[13] ^ MUL2[state[14]] ^ MUL3[state[15]]);
        tmp[15] = (char) (MUL3[state[12]] ^ state[13] ^ state[14] ^ MUL2[state[15]]);

        System.arraycopy(tmp, 0, state, 0, 16);

        return state;

    }

    private static final char[] MUL2 = {
            0x00,0x02,0x04,0x06,0x08,0x0a,0x0c,0x0e,0x10,0x12,0x14,0x16,0x18,0x1a,0x1c,0x1e,
            0x20,0x22,0x24,0x26,0x28,0x2a,0x2c,0x2e,0x30,0x32,0x34,0x36,0x38,0x3a,0x3c,0x3e,
            0x40,0x42,0x44,0x46,0x48,0x4a,0x4c,0x4e,0x50,0x52,0x54,0x56,0x58,0x5a,0x5c,0x5e,
            0x60,0x62,0x64,0x66,0x68,0x6a,0x6c,0x6e,0x70,0x72,0x74,0x76,0x78,0x7a,0x7c,0x7e,
            0x80,0x82,0x84,0x86,0x88,0x8a,0x8c,0x8e,0x90,0x92,0x94,0x96,0x98,0x9a,0x9c,0x9e,
            0xa0,0xa2,0xa4,0xa6,0xa8,0xaa,0xac,0xae,0xb0,0xb2,0xb4,0xb6,0xb8,0xba,0xbc,0xbe,
            0xc0,0xc2,0xc4,0xc6,0xc8,0xca,0xcc,0xce,0xd0,0xd2,0xd4,0xd6,0xd8,0xda,0xdc,0xde,
            0xe0,0xe2,0xe4,0xe6,0xe8,0xea,0xec,0xee,0xf0,0xf2,0xf4,0xf6,0xf8,0xfa,0xfc,0xfe,
            0x1b,0x19,0x1f,0x1d,0x13,0x11,0x17,0x15,0x0b,0x09,0x0f,0x0d,0x03,0x01,0x07,0x05,
            0x3b,0x39,0x3f,0x3d,0x33,0x31,0x37,0x35,0x2b,0x29,0x2f,0x2d,0x23,0x21,0x27,0x25,
            0x5b,0x59,0x5f,0x5d,0x53,0x51,0x57,0x55,0x4b,0x49,0x4f,0x4d,0x43,0x41,0x47,0x45,
            0x7b,0x79,0x7f,0x7d,0x73,0x71,0x77,0x75,0x6b,0x69,0x6f,0x6d,0x63,0x61,0x67,0x65,
            0x9b,0x99,0x9f,0x9d,0x93,0x91,0x97,0x95,0x8b,0x89,0x8f,0x8d,0x83,0x81,0x87,0x85,
            0xbb,0xb9,0xbf,0xbd,0xb3,0xb1,0xb7,0xb5,0xab,0xa9,0xaf,0xad,0xa3,0xa1,0xa7,0xa5,
            0xdb,0xd9,0xdf,0xdd,0xd3,0xd1,0xd7,0xd5,0xcb,0xc9,0xcf,0xcd,0xc3,0xc1,0xc7,0xc5,
            0xfb,0xf9,0xff,0xfd,0xf3,0xf1,0xf7,0xf5,0xeb,0xe9,0xef,0xed,0xe3,0xe1,0xe7,0xe5
    };

    private static final char[] MUL3 = {
            0x00,0x03,0x06,0x05,0x0c,0x0f,0x0a,0x09,0x18,0x1b,0x1e,0x1d,0x14,0x17,0x12,0x11,
            0x30,0x33,0x36,0x35,0x3c,0x3f,0x3a,0x39,0x28,0x2b,0x2e,0x2d,0x24,0x27,0x22,0x21,
            0x60,0x63,0x66,0x65,0x6c,0x6f,0x6a,0x69,0x78,0x7b,0x7e,0x7d,0x74,0x77,0x72,0x71,
            0x50,0x53,0x56,0x55,0x5c,0x5f,0x5a,0x59,0x48,0x4b,0x4e,0x4d,0x44,0x47,0x42,0x41,
            0xc0,0xc3,0xc6,0xc5,0xcc,0xcf,0xca,0xc9,0xd8,0xdb,0xde,0xdd,0xd4,0xd7,0xd2,0xd1,
            0xf0,0xf3,0xf6,0xf5,0xfc,0xff,0xfa,0xf9,0xe8,0xeb,0xee,0xed,0xe4,0xe7,0xe2,0xe1,
            0xa0,0xa3,0xa6,0xa5,0xac,0xaf,0xaa,0xa9,0xb8,0xbb,0xbe,0xbd,0xb4,0xb7,0xb2,0xb1,
            0x90,0x93,0x96,0x95,0x9c,0x9f,0x9a,0x99,0x88,0x8b,0x8e,0x8d,0x84,0x87,0x82,0x81,
            0x9b,0x98,0x9d,0x9e,0x97,0x94,0x91,0x92,0x83,0x80,0x85,0x86,0x8f,0x8c,0x89,0x8a,
            0xab,0xa8,0xad,0xae,0xa7,0xa4,0xa1,0xa2,0xb3,0xb0,0xb5,0xb6,0xbf,0xbc,0xb9,0xba,
            0xfb,0xf8,0xfd,0xfe,0xf7,0xf4,0xf1,0xf2,0xe3,0xe0,0xe5,0xe6,0xef,0xec,0xe9,0xea,
            0xcb,0xc8,0xcd,0xce,0xc7,0xc4,0xc1,0xc2,0xd3,0xd0,0xd5,0xd6,0xdf,0xdc,0xd9,0xda,
            0x5b,0x58,0x5d,0x5e,0x57,0x54,0x51,0x52,0x43,0x40,0x45,0x46,0x4f,0x4c,0x49,0x4a,
            0x6b,0x68,0x6d,0x6e,0x67,0x64,0x61,0x62,0x73,0x70,0x75,0x76,0x7f,0x7c,0x79,0x7a,
            0x3b,0x38,0x3d,0x3e,0x37,0x34,0x31,0x32,0x23,0x20,0x25,0x26,0x2f,0x2c,0x29,0x2a,
            0x0b,0x08,0x0d,0x0e,0x07,0x04,0x01,0x02,0x13,0x10,0x15,0x16,0x1f,0x1c,0x19,0x1a
    };

    private static char[] AddRoundKey(char[] state, char[] roundKey){
        for(int i=0; i<16; i++)
            state[i] ^= roundKey[i];

        return state;
    }

    private static char[] KeyExpansionCore(char[] in, char i){
        // Rotate left
        char h = in[0];
        in[0] = in[1];
        in[1] = in[2];
        in[2] = in[3];
        in[3] = h;

        // S-Box four bytes
        in[0] = SUB[in[0]];
        in[1] = SUB[in[1]];
        in[2] = SUB[in[2]];
        in[3] = SUB[in[3]];

        // RCon
        in[0] ^= RCON[i];

        return in;
    }

    private static final char[] RCON = {
            0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a,
            0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39,
            0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a,
            0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8,
            0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef,
            0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc,
            0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b,
            0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3,
            0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94,
            0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20,
            0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35,
            0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f,
            0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04,
            0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63,
            0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd,
            0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d
    };

    private static final char[] MY_ALPHABET = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '.', ',', '-', '?', ':', '_', '"', '!',
            '/', '|', 0x5c, ';', '*', '+', '@', '€', '(', ')',
            '$', '&', '%', '^', '<', '>', '~', '[', ']', 0x27
    }; // 5c - \  22 - "  27 - '  .,-?:_"!/;*+@()%       |\€$&^<>~'[]{}

    private static final char[] SMALL_ALPHABET = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    private static final char[] BIG_ALPHABET = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private static final char[] NUMBERS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    private static final char[] BASIC_CHARS = {
            '.', ',', '-', '?', ':', '_', '!', '/', ';', '*',
            '+', '@', '(', ')', '%'
    };

    private static final char[] ADVANCED_CHARS = {
            '|', '€', '$', '[', ']', '{', '}'
    };
}